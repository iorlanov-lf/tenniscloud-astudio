package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class LeagueProfile extends DynamicEntity {
    String userId;
    String leagueMetroAreaId;
    LeagueMetroArea leagueMetroArea;
    String firstLastName;
    String email;
    String phoneNumber;

    @Override
    public String getParentId() {
        return userId;
    }

    public LeagueProfile() {
        super();
    }

    public LeagueProfile(String id, Long version, Integer syncState,
                  String userId, String leagueMetroAreaId, String firstLastName, String email, String phoneNumber) {
        super(id, version, syncState);

        this.userId = userId;
        this.leagueMetroAreaId = leagueMetroAreaId;
        this.firstLastName = firstLastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
