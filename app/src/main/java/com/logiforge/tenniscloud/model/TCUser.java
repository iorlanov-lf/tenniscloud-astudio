package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/19/2017.
 */
public class TCUser extends DynamicEntity {
    String userId;
    String firstLastName;
    String email;
    String phoneNbr;

    @Override
    public String getParentId() {
        return null;
    }

    public TCUser() {
        super();
    }

    public TCUser(String id, Long version, Integer syncState,
                  String userId, String firstLastName, String email, String phoneNbr) {
        super(id, version, syncState);

        this.userId = userId;
        this.firstLastName = firstLastName;
        this.email = email;
        this.phoneNbr = phoneNbr;
    }
}
