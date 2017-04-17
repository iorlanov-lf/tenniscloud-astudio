package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.List;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class MatchPlayer extends DynamicEntity {
    String matchId;
    Boolean isHomeTeam;
    List<MatchAvailability> availabilityList;

    // simple player
    String firstLastName;
    String email;
    String phoneNbr;

    // TCUser player
    String userId;
    TCUser tcUser;

    // contact player
    String contactId;
    TennisContact tennisContact;

    // league player
    String leagueProfileId;
    LeagueProfile leagueProfile;

    @Override
    public String getParentId() {
        return matchId;
    }

    public MatchPlayer() {
        super();
    }

    public MatchPlayer(String id, Long version, Integer syncState,
                       String matchId, Boolean isHomeTeam,
                       String firstLastName, String email, String phoneNbr,
                       String userId, String contactId, String leagueProfileId) {
        super(id, version, syncState);

        this.matchId = matchId;
        this.isHomeTeam = isHomeTeam;
        this.firstLastName = firstLastName;
        this.email = email;
        this.phoneNbr = phoneNbr;
        this.userId = userId;
        this.contactId = contactId;
        this.leagueProfileId = leagueProfileId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Boolean getHomeTeam() {
        return isHomeTeam;
    }

    public void setHomeTeam(Boolean homeTeam) {
        isHomeTeam = homeTeam;
    }

    public List<MatchAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<MatchAvailability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TCUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TCUser tcUser) {
        this.tcUser = tcUser;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public TennisContact getTennisContact() {
        return tennisContact;
    }

    public void setTennisContact(TennisContact tennisContact) {
        this.tennisContact = tennisContact;
    }

    public String getLeagueProfileId() {
        return leagueProfileId;
    }

    public void setLeagueProfileId(String leagueProfileId) {
        this.leagueProfileId = leagueProfileId;
    }

    public LeagueProfile getLeagueProfile() {
        return leagueProfile;
    }

    public void setLeagueProfile(LeagueProfile leagueProfile) {
        this.leagueProfile = leagueProfile;
    }
}
