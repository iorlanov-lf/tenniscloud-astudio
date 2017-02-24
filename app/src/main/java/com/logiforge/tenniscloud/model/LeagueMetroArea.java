package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/14/2017.
 */
public class LeagueMetroArea extends DynamicEntity {
    String providerId;
    LeagueProvider provider;
    String state;
    String metroAreaName;

    @Override
    public String getParentId() {
        return providerId;
    }

    public LeagueMetroArea() {
        super();
    }

    public LeagueMetroArea(String id, Long version, Integer syncState, String providerId, String state, String metroAreaName) {
        super(id, version, syncState);

        this.providerId = providerId;
        this.state = state;
        this.metroAreaName = metroAreaName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public LeagueProvider getProvider() {
        return provider;
    }

    public void setProvider(LeagueProvider provider) {
        this.provider = provider;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMetroAreaName() {
        return metroAreaName;
    }

    public void setMetroAreaName(String metroAreaName) {
        this.metroAreaName = metroAreaName;
    }
}
