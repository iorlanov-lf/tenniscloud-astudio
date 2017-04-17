package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/21/2017.
 */
public class LeagueRegistration extends DynamicEntity {
    String leagueProfileId;
    LeagueProfile profile;
    String leagueFlightId;
    LeagueFlight leagueFlight;
    String leagueDivisionId;
    LeagueDivision leagueDivision;
    String facilityId;
    Facility facility;

    @Override
    public String getParentId() {
        return leagueProfileId;
    }

    public LeagueRegistration() {
        super();
    }

    public LeagueRegistration(String id, Long version, Integer syncState,
                  String leagueProfileId, String leagueFlightId, String leagueDivisionId, String facilityId) {
        super(id, version, syncState);

        this.leagueProfileId = leagueProfileId;
        this.leagueFlightId = leagueFlightId;
        this.leagueDivisionId = leagueDivisionId;
        this.facilityId = facilityId;
    }

    public String getLeagueProfileId() {
        return leagueProfileId;
    }

    public void setLeagueProfileId(String leagueProfileId) {
        this.leagueProfileId = leagueProfileId;
    }

    public LeagueProfile getProfile() {
        return profile;
    }

    public void setProfile(LeagueProfile profile) {
        this.profile = profile;
    }

    public String getLeagueFlightId() {
        return leagueFlightId;
    }

    public void setLeagueFlightId(String leagueFlightId) {
        this.leagueFlightId = leagueFlightId;
    }

    public LeagueFlight getLeagueFlight() {
        return leagueFlight;
    }

    public void setLeagueFlight(LeagueFlight leagueFlight) {
        this.leagueFlight = leagueFlight;
    }

    public String getLeagueDivisionId() {
        return leagueDivisionId;
    }

    public void setLeagueDivisionId(String leagueDivisionId) {
        this.leagueDivisionId = leagueDivisionId;
    }

    public LeagueDivision getLeagueDivision() {
        return leagueDivision;
    }

    public void setLeagueDivision(LeagueDivision leagueDivision) {
        this.leagueDivision = leagueDivision;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
