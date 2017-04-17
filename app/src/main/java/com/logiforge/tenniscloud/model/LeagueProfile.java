package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class LeagueProfile extends DynamicEntity {
    String userName;
    String leagueMetroAreaId;
    LeagueMetroArea leagueMetroArea;
    String displayName;
    String email;
    String phoneNumber;

    @Override
    public String getParentId() {
        return userName;
    }

    public LeagueProfile() {
        super();
    }

    public LeagueProfile(String id, Long version, Integer syncState,
                  String userName, String leagueMetroAreaId, String displayName, String email, String phoneNumber) {
        super(id, version, syncState);

        this.userName = userName;
        this.leagueMetroAreaId = leagueMetroAreaId;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
