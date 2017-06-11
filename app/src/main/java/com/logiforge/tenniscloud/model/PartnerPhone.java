package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class PartnerPhone extends DynamicEntity {

    String partnerId;
    String phone;
    Integer phoneType;

    @Override
    public String getParentId() {
        return partnerId;
    }

    public PartnerPhone() {
        super();
    }

    public PartnerPhone(String partnerId, String phone, Integer phoneType) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.partnerId = partnerId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public PartnerPhone(String id, Long version, Integer syncState,
                        String partnerId, String phone, Integer phoneType) {
        super(id, version, syncState);

        this.partnerId = partnerId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getPartnerId() {
        return partnerId;
    }
    
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }
}
