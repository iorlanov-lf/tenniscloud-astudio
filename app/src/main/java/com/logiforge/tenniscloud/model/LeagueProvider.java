package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class LeagueProvider extends DynamicEntity {
    String providerName;

    @Override
    public String getParentId() {
        return null;
    }

    public LeagueProvider() {
        super();
    }

    public LeagueProvider(String id, Long version, Integer syncState, String providerName) {
        super(id, version, syncState);

        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
