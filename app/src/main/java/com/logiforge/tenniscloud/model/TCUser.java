package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.List;

/**
 * Created by iorlanov on 2/19/2017.
 */
public class TCUser extends DynamicEntity {
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMAIL = 1;

    String userName;
    String displayName;
    String phoneNbr;
    Integer gender;
    List<TCUserEmail> emails;

    @Override
    public String getParentId() {
        return null;
    }

    public TCUser() {
        super();
    }

    public TCUser(String id, Long version, Integer syncState,
                  String userName, String displayName, String phoneNbr, Integer gender) {
        super(id, version, syncState);

        this.userName = userName;
        this.displayName = displayName;
        this.phoneNbr = phoneNbr;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userId) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<TCUserEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<TCUserEmail> emails) {
        this.emails = emails;
    }
}
