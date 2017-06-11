package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class MatchPlayerPhone extends DynamicEntity {

    String matchPlayerId;
    String phone;
    Integer phoneType;

    @Override
    public String getParentId() {
        return matchPlayerId;
    }

    public MatchPlayerPhone() {
        super();
    }

    public MatchPlayerPhone(String matchPlayerId, String phone, Integer phoneType) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.matchPlayerId = matchPlayerId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public MatchPlayerPhone(String id, Long version, Integer syncState,
                            String matchPlayerId, String phone, Integer phoneType) {
        super(id, version, syncState);

        this.matchPlayerId = matchPlayerId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getMatchPlayerId() {
        return matchPlayerId;
    }
    
    public void setMatchPlayertId(String matchPlayerId) {
        this.matchPlayerId = matchPlayerId;
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
