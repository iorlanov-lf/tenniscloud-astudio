package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.List;

/**
 * Created by iorlanov on 2/17/2017.
 */
public class TennisContact extends DynamicEntity {
    String userId;

    // non-TC contact
    String displayName;
    List<TennisContactPhone> phones;
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
                  String userId, String displayName, String otherUserId) {
        super(id, version, syncState);

        this.userId = userId;
        this.displayName = displayName;
        this.otherUserId = otherUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public List<TennisContactPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<TennisContactPhone> phones) {
        this.phones = phones;
    }
}
