package com.logiforge.tenniscloud.facades;

import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.facade.LavoltaFacade;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.LeagueFlightTbl;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProfileTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueRegistrationTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueFlight;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/13/2017.
 */
public class LeagueRegistrationFacade {
    private static final String TAG = LeagueRegistrationFacade.class.getSimpleName();

    public boolean hasRegistration() {
        LeagueRegistrationTbl regTbl = new LeagueRegistrationTbl();

        List<LeagueProfile> profiles = getProfiles();
        for(LeagueProfile profile : profiles) {
            List<LeagueRegistration> profileRegistrations = regTbl.getProfileRegistrations(profile.id);
            if(profileRegistrations.size() > 0) {
                return true;
            }
        }

        return false;
    }

    public List<LeagueProfile> getProfiles() {
        LeagueProfileTbl profileTbl = new LeagueProfileTbl();
        LavoltaFacade lavoltaFacade = new LavoltaFacade();
        return profileTbl.getUserProfiles(lavoltaFacade.getUserName());
    }

    public List<LeagueRegistration> getRegistrations(boolean resolveReferences) {
        LeagueRegistrationTbl regTbl = new LeagueRegistrationTbl();
        LeagueFlightTbl flightTbl = new LeagueFlightTbl();
        FacilityTbl facilityTbl = new FacilityTbl();
        LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
        LeagueProviderTbl providerTbl = new LeagueProviderTbl();
        PlayingLevelTbl levelTbl = new PlayingLevelTbl();
        LeagueTbl leagueTbl = new LeagueTbl();

        List<LeagueRegistration> registrations = new ArrayList<LeagueRegistration>();

        List<LeagueProfile> profiles = getProfiles();
        for(LeagueProfile profile : profiles) {
            List<LeagueRegistration> profileRegistrations = regTbl.getProfileRegistrations(profile.id);
            registrations.addAll(profileRegistrations);

            if(resolveReferences) {
                for (LeagueRegistration registration : profileRegistrations) {
                    // profile
                    registration.setProfile(profile);
                    // area
                    LeagueMetroArea area = (LeagueMetroArea)areaTbl.find(profile.getLeagueMetroAreaId());
                    profile.setLeagueMetroArea(area);
                    // provider
                    LeagueProvider provider = (LeagueProvider)providerTbl.find(area.getProviderId());
                    area.setProvider(provider);

                    // flight
                    LeagueFlight flight = (LeagueFlight)flightTbl.find(registration.getLeagueFlightId());
                    registration.setLeagueFlight(flight);
                    // level
                    PlayingLevel level = (PlayingLevel)levelTbl.find(flight.getPlayingLevelId());
                    flight.setPlayingLevel(level);
                    // league
                    League league = (League)leagueTbl.find(flight.getLeagueId());
                    flight.setLeague(league);

                    // facility
                    Facility facility = (Facility)facilityTbl.find(registration.getFacilityId());
                    registration.setFacility(facility);
                }
            }
        }

        return registrations;
    }

    public LeagueRegistration createLeagueRegistration(
            LeagueProvider provider, LeagueMetroArea area, League league,
            PlayingLevel level, LeagueProfile profile, Facility facility) {

        LeagueRegistration registration = null;

        LeagueRegistrationTbl lregTbl = new LeagueRegistrationTbl();
        LeagueFlightTbl flightTbl = new LeagueFlightTbl();
        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_NEW_LEAGUE_REGISTRATION);

            LeagueFlight flight = flightTbl.findByLeagueAndLevel(league.id, level.id);
            if(flight == null) {
                flight = new LeagueFlight();
                flight.setLeagueId(league.id);
                flight.setPlayingLevelId(level.id);
                flightTbl.uiAdd(txn, flight, null);
            }

            if(profile.id == null) {
                LavoltaFacade lavoltaFacade = new LavoltaFacade();
                LeagueProfileTbl profileTbl = new LeagueProfileTbl();
                profile.setUserName(lavoltaFacade.getUserName());
                profile.setLeagueMetroAreaId(area.id);
                profileTbl.uiAdd(txn, profile, null);
            }

            registration = new LeagueRegistration();
            registration.setLeagueProfileId(profile.id);
            registration.setLeagueFlightId(flight.id);
            registration.setFacilityId(facility.id);
            lregTbl.uiAdd(txn, registration, null);

            // set relationships
            registration.setProfile(profile);
            registration.setLeagueFlight(flight);
            registration.setFacility(facility);

            profile.setLeagueMetroArea(area);
            area.setProvider(provider);
            flight.setPlayingLevel(level);
            flight.setLeague(league);
            league.setLeagueMetroArea(area);

            db.commitTxn(txn);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName():e.getMessage());
        } finally {
            db.endTxn(txn, false);
        }

        return registration;
    }
}
