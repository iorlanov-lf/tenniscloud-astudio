package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class LeagueProfilePhone extends DynamicEntity {

    String leagueProfileId;
    String phone;
    Integer phoneType;

    @Override
    public String getParentId() {
        return leagueProfileId;
    }

    public LeagueProfilePhone() {
        super();
    }

    public LeagueProfilePhone(String leagueProfileId, String phone, Integer phoneType) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.leagueProfileId = leagueProfileId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public LeagueProfilePhone(String id, Long version, Integer syncState,
                              String leagueProfileId, String phone, Integer phoneType) {
        super(id, version, syncState);

        this.leagueProfileId = leagueProfileId;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public String getLeagueProfileId() {
        return leagueProfileId;
    }

    public void setLeagueProfileId(String leagueProfileId) {
        this.leagueProfileId = leagueProfileId;
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
