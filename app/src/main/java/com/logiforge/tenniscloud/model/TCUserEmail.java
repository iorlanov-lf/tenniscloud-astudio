package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TCUserEmail extends DynamicEntity {

    String userId;
    String email;
    Boolean confirmed;

    @Override
    public String getParentId() {
        return userId;
    }

    public TCUserEmail() {
        super();
    }

    public TCUserEmail(String id, Long version, Integer syncState,
                  String userId, String email, Boolean confirmed) {
        super(id, version, syncState);

        this.userId = userId;
        this.email = email;
        this.confirmed = confirmed;
    }

    public TCUserEmail(String userId, String email, Boolean confirmed) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.userId = userId;
        this.email = email;
        this.confirmed = confirmed;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
