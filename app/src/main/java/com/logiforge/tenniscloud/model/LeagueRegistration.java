package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/21/2017.
 */
public class LeagueRegistration extends DynamicEntity {
    String userId;
    String leagueId;
    League league;
    String facilityId;
    Facility facility;

    @Override
    public String getParentId() {
        return userId;
    }

    public LeagueRegistration() {
        super();
    }

    public LeagueRegistration(String id, Long version, Integer syncState,
                  String userId, String leagueId, String facilityId) {
        super(id, version, syncState);

        this.userId = userId;
        this.leagueId = leagueId;
        this.facilityId = facilityId;
    }
}
