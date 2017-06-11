package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TCUserPhone extends DynamicEntity {

    String userId;
    String phone;
    Integer phoneType;

    @Override
    public String getParentId() {
        return userId;
    }

    public TCUserPhone() {
        super();
    }

    public TCUserPhone(String userId, String phone, Integer phoneType) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.userId = userId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public TCUserPhone(String id, Long version, Integer syncState,
                       String userId, String phone, Integer phoneType) {
        super(id, version, syncState);

        this.userId = userId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
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
