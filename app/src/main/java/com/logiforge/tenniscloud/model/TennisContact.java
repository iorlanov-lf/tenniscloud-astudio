package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.List;

/**
 * Created by iorlanov on 2/17/2017.
 */
public class TennisContact extends DynamicEntity {
    String userId;

    // non-TC contact
    String firstLastName;
    String phoneNbr;
    List<TennisContactEmail> emails;

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
                  String userId, String firstLastName, String phoneNbr, String otherUserId) {
        super(id, version, syncState);

        this.userId = userId;
        this.firstLastName = firstLastName;
        this.phoneNbr = phoneNbr;
        this.otherUserId = otherUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public TCUser getOtherTcUser() {
        return otherTcUser;
    }

    public void setOtherTcUser(TCUser otherTcUser) {
        this.otherTcUser = otherTcUser;
    }

    public List<TennisContactEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<TennisContactEmail> emails) {
        this.emails = emails;
    }
}
