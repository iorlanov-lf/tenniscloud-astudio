package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/17/2017.
 */
public class TennisContact extends DynamicEntity {
    String userId;

    // non-TC contact
    String firstLastName;
    String email;
    String phoneNbr;

    String otherUserId;
    TCUser otherTcUser;

    @Override
    public String getParentId() {
        return userId;
    }

    public TennisContact() {
        super();
    }

    public TennisContact(String id, Long version, Integer syncState,
                  String userId, String firstLastName, String email, String phoneNbr, String otherUserId) {
        super(id, version, syncState);

        this.userId = userId;
        this.firstLastName = firstLastName;
        this.email = email;
        this.phoneNbr = phoneNbr;
        this.otherUserId = otherUserId;
    }
}
