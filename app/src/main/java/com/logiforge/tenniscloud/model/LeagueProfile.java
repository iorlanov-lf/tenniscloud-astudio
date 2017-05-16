package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class LeagueProfile extends DynamicEntity {
    String userId;
    String leagueMetroAreaId;
    LeagueMetroArea leagueMetroArea;
    String displayName;
    String phoneNumber;
    List<LeagueProfileEmail> emails;

    @Override
    public String getParentId() {
        return userId;
    }

    public LeagueProfile() {
        super();
    }

    public LeagueProfile(String id, Long version, Integer syncState,
                  String userId, String leagueMetroAreaId, String displayName, String phoneNumber) {
        super(id, version, syncState);

        this.userId = userId;
        this.leagueMetroAreaId = leagueMetroAreaId;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLeagueMetroAreaId() {
        return leagueMetroAreaId;
    }

    public void setLeagueMetroAreaId(String leagueMetroAreaId) {
        this.leagueMetroAreaId = leagueMetroAreaId;
    }

    public LeagueMetroArea getLeagueMetroArea() {
        return leagueMetroArea;
    }

    public void setLeagueMetroArea(LeagueMetroArea leagueMetroArea) {
        this.leagueMetroArea = leagueMetroArea;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<LeagueProfileEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<LeagueProfileEmail> emails) {
        this.emails = emails;
    }

    public void setEmails2(List<String> emailList) {
        emails = new ArrayList<LeagueProfileEmail>();
        for(String email : emailList) {
            emails.add(new LeagueProfileEmail(null, email));
        }
    }
}
