package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TCUserEmail extends DynamicEntity {

    String userName;
    String email;
    Boolean confirmed;

    @Override
    public String getParentId() {
        return userName;
    }

    public TCUserEmail() {
        super();
    }

    public TCUserEmail(String id, Long version, Integer syncState,
                  String userName, String email, Boolean confirmed) {
        super(id, version, syncState);

        this.userName = userName;
        this.email = email;
        this.confirmed = confirmed;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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
