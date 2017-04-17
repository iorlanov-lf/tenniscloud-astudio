package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/22/2017.
 */
public class TCUserFacility extends DynamicEntity {
    String userId;
    String facilityId;
    Facility facility;

    @Override
    public String getParentId() {
        return userId;
    }

    public TCUserFacility() {
        super();
    }

    public TCUserFacility(String id, Long version, Integer syncState,
        String userId, String facilityId) {
        super(id, version, syncState);

        this.userId = userId;
        this.facilityId = facilityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
