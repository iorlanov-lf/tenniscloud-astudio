package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 3/1/2017.
 */
public class PlayingLevel extends DynamicEntity {
    String providerId;
    Integer ordinalNumber;
    String description;

    @Override
    public String getParentId() {
        return providerId;
    }

    public PlayingLevel() {
        super();
    }

    public PlayingLevel(String id, Long version, Integer syncState,
                        String providerId, Integer ordinalNumber, String description) {
        super(id, version, syncState);

        this.providerId = providerId;
        this.ordinalNumber = ordinalNumber;
        this.description = description;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
