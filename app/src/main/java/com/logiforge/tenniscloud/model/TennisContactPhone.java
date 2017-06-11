package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TennisContactPhone extends DynamicEntity {

    String tennisContactId;
    String phone;
    Integer phoneType;

    @Override
    public String getParentId() {
        return tennisContactId;
    }

    public TennisContactPhone() {
        super();
    }

    public TennisContactPhone(String tennisContactId, String phone, Integer phoneType) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.tennisContactId = tennisContactId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public TennisContactPhone(String id, Long version, Integer syncState,
                              String tennisContactId, String phone, Integer phoneType) {
        super(id, version, syncState);

        this.tennisContactId = tennisContactId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getTennisContactId() {
        return tennisContactId;
    }
    
    public void setTennisContactId(String tennisContactId) {
        this.tennisContactId = tennisContactId;
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
