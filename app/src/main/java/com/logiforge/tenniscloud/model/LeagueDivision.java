package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/25/2017.
 */
public class LeagueDivision extends DynamicEntity {
    String leagueFlightId;
    String providerAssignedId;

    @Override
    public String getParentId() {
        return leagueFlightId;
    }

    public LeagueDivision() {
        super();
    }

    public LeagueDivision(String id, Long version, Integer syncState,
                  String leagueFlightId, String providerAssignedId) {
        super(id, version, syncState);

        this.leagueFlightId = leagueFlightId;
        this.providerAssignedId = providerAssignedId;
    }

    public String getLeagueFlightId() {
        return leagueFlightId;
    }

    public void setLeagueFlightId(String leagueFlightId) {
        this.leagueFlightId = leagueFlightId;
    }

    public String getProviderAssignedId() {
        return providerAssignedId;
    }

    public void setProviderAssignedId(String providerAssignedId) {
        this.providerAssignedId = providerAssignedId;
    }
}
