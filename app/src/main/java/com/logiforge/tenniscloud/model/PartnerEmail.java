package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class PartnerEmail extends DynamicEntity {

    String partnerId;
    String email;

    @Override
    public String getParentId() {
        return partnerId;
    }

    public PartnerEmail() {
        super();
    }

    public PartnerEmail(String id, Long version, Integer syncState,
                        String partnerId, String email) {
        super(id, version, syncState);

        this.partnerId = partnerId;
        this.email = email;
    }

    public PartnerEmail(String partnerId, String email) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.partnerId = partnerId;
        this.email = email;
    }

    public String getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
