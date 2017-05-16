package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TennisContactEmail extends DynamicEntity {

    String tennisContactId;
    String email;

    @Override
    public String getParentId() {
        return tennisContactId;
    }

    public TennisContactEmail() {
        super();
    }

    public TennisContactEmail(String id, Long version, Integer syncState,
                              String tennisContactId, String email) {
        super(id, version, syncState);

        this.tennisContactId = tennisContactId;
        this.email = email;
    }

    public String getTennisContactId() {
        return tennisContactId;
    }
    public void setTennisContactId(String tennisContactId) {
        this.tennisContactId = tennisContactId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
