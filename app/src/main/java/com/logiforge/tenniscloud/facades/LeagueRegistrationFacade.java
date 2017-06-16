package com.logiforge.tenniscloud.facades;

import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.facade.LavoltaFacade;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.LeagueFlightTbl;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProfileEmailTbl;
import com.logiforge.tenniscloud.db.LeagueProfilePhoneTbl;
import com.logiforge.tenniscloud.db.LeagueProfileTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueRegistrationTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.PartnerEmailTbl;
import com.logiforge.tenniscloud.db.PartnerPhoneTbl;
import com.logiforge.tenniscloud.db.PartnerTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.db.TCUserEmailTbl;
import com.logiforge.tenniscloud.db.TCUserPhoneTbl;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueFlight;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.LeagueProfileEmail;
import com.logiforge.tenniscloud.model.LeagueProfilePhone;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.PartnerEmail;
import com.logiforge.tenniscloud.model.PartnerPhone;
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.TCUser;
import com.logiforge.tenniscloud.model.TCUserEmail;
import com.logiforge.tenniscloud.model.TCUserPhone;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/13/2017.
 */
public class LeagueRegistrationFacade {
    public static class Builder {
        LeagueRegistration registration = null;
        boolean resolveFacilityFlag = false;

        public Builder(LeagueRegistration registration) {
            this.registration = registration;
        }

        public Builder resolveFacility() {
            resolveFacilityFlag = true;
            return this;
        }

        public LeagueRegistration build() {
            if(registration != null) {
                if (resolveFacilityFlag &&
                        registration.getFacilityId() != null &&
                        registration.getFacility() == null) {
                    FacilityTbl facilityTbl = new FacilityTbl();
                    registration.setFacility(
                            (Facility)facilityTbl.find(registration.getFacilityId()));
                }
            }

            return registration;
        }
    }

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
        TCUserFacade userFacade = new TCUserFacade();
        return profileTbl.getByUserId(userFacade.getSelf().id);
    }

    public List<LeagueRegistration> getRegistrations(boolean resolveReferences) {
        LeagueRegistrationTbl regTbl = new LeagueRegistrationTbl();
        LeagueFlightTbl flightTbl = new LeagueFlightTbl();
        FacilityTbl facilityTbl = new FacilityTbl();
        LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
        LeagueProviderTbl providerTbl = new LeagueProviderTbl();
        PlayingLevelTbl levelTbl = new PlayingLevelTbl();
        LeagueTbl leagueTbl = new LeagueTbl();
        PartnerTbl partnerTbl = new PartnerTbl();

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

                    // partner
                    PartnerFacade.Builder partnerBuilder =
                            new PartnerFacade.Builder(partnerTbl.getByRegistrationId(registration.id));
                    Partner partner = partnerBuilder.resolveEmails().resolvePhones().build();
                    registration.setPartner(partner);
                }
            }
        }

        return registrations;
    }

    public LeagueRegistration createLeagueRegistration(
            LeagueProvider provider, LeagueMetroArea area, League league,
            PlayingLevel level, Facility facility, LeagueProfile profile, Partner partner) {

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
                TCUser self = TCUserFacade.builder().resolveEmails().resolvePhones().build();
                profile.setUserId(self.id);
                profile.setLeagueMetroAreaId(area.id);
                LeagueProfileTbl profileTbl = new LeagueProfileTbl();
                profileTbl.uiAdd(txn, profile, null);

                LeagueProfileEmailTbl profileEmailTbl = new LeagueProfileEmailTbl();
                TCUserEmailTbl userEmailTbl = new TCUserEmailTbl();
                for(LeagueProfileEmail email : profile.getEmails()) {
                    email.setLeagueProfileId(profile.id);
                    profileEmailTbl.uiAdd(txn, email, null);

                    // see if there are new emails
                    boolean found = false;
                    for (TCUserEmail userEmail : self.getEmails())
                    {
                        if(userEmail.getEmail().equalsIgnoreCase(email.getEmail())) {
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        TCUserEmail newUserEmail = new TCUserEmail(self.id, email.getEmail(), false);
                        userEmailTbl.uiAdd(txn, newUserEmail, null);
                    }
                }

                LeagueProfilePhoneTbl profilePhoneTbl = new LeagueProfilePhoneTbl();
                TCUserPhoneTbl userPhoneTbl = new TCUserPhoneTbl();
                for(LeagueProfilePhone phone : profile.getPhones()) {
                    phone.setLeagueProfileId(profile.id);
                    profilePhoneTbl.uiAdd(txn, phone, null);

                    // see if there are new phones
                    boolean found = false;
                    for (TCUserPhone userPhone : self.getPhones())
                    {
                        if( Phone.compareNumbers(userPhone.getPhone(), phone.getPhone()) ) {
                            found = true;
                            break;
                        }
                    }

                    if(!found) {
                        TCUserPhone newUserPhone = new TCUserPhone(self.id, phone.getPhone(), phone.getPhoneType());
                        userPhoneTbl.uiAdd(txn, newUserPhone, null);
                    }
                }
            }

            registration = new LeagueRegistration();
            registration.setLeagueProfileId(profile.id);
            registration.setLeagueFlightId(flight.id);
            registration.setFacilityId(facility.id);
            lregTbl.uiAdd(txn, registration, null);

            if(partner != null) {
                PartnerTbl partnerTbl = new PartnerTbl();
                partner.setLeagueRegistrationId(registration.id);
                partnerTbl.uiAdd(txn, partner, null);

                PartnerEmailTbl partnerEmailTbl = new PartnerEmailTbl();
                for(PartnerEmail email : partner.getEmails()) {
                    email.setPartnerId(partner.id);
                    partnerEmailTbl.uiAdd(txn, email, null);
                }

                PartnerPhoneTbl partnerPhoneTbl = new PartnerPhoneTbl();
                for(PartnerPhone phone : partner.getPhones()) {
                    phone.setPartnerId(partner.id);
                    partnerPhoneTbl.uiAdd(txn, phone, null);
                }
            }

            db.commitTxn(txn);

            // set relationships
            registration.setProfile(profile);
            registration.setLeagueFlight(flight);
            registration.setFacility(facility);
            registration.setPartner(partner);

            profile.setLeagueMetroArea(area);
            area.setProvider(provider);

            flight.setPlayingLevel(level);
            flight.setLeague(league);
            league.setLeagueMetroArea(area);

        } catch(Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName():e.getMessage());
        } finally {
            db.endTxn(txn, false);
        }

        return registration;
    }
}
