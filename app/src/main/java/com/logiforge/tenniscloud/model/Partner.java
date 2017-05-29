package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class Partner extends DynamicEntity {
    String leagueRegistrationId;

    // simple player
    String firstLastName;
    String phoneNbr;
    List<PartnerEmail> emails;

    // TCUser player
    String userId;
    TCUser tcUser;

    // contact player
    String contactId;
    TennisContact tennisContact;

    // league player
    String leagueProfileId;
    LeagueProfile leagueProfile;

    @Override
    public String getParentId() {
        return leagueRegistrationId;
    }

    public Partner() {
        super();
    }

    public Partner(String id, Long version, Integer syncState,
                   String leagueRegistrationId,
                   String firstLastName, String phoneNbr,
                   String userId, String contactId, String leagueProfileId) {
        super(id, version, syncState);

        this.leagueRegistrationId = leagueRegistrationId;
        this.firstLastName = firstLastName;
        this.phoneNbr = phoneNbr;
        this.userId = userId;
        this.contactId = contactId;
        this.leagueProfileId = leagueProfileId;
    }

    public String getLeagueRegistrationId() {
        return leagueRegistrationId;
    }

    public void setLeagueRegistrationId(String leagueRegistrationId) {
        this.leagueRegistrationId = leagueRegistrationId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TCUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TCUser tcUser) {
        this.tcUser = tcUser;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public TennisContact getTennisContact() {
        return tennisContact;
    }

    public void setTennisContact(TennisContact tennisContact) {
        this.tennisContact = tennisContact;
    }

    public String getLeagueProfileId() {
        return leagueProfileId;
    }

    public void setLeagueProfileId(String leagueProfileId) {
        this.leagueProfileId = leagueProfileId;
    }

    public LeagueProfile getLeagueProfile() {
        return leagueProfile;
    }

    public void setLeagueProfile(LeagueProfile leagueProfile) {
        this.leagueProfile = leagueProfile;
    }

    public List<PartnerEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<PartnerEmail> emails) {
        this.emails = emails;
    }

    public void setEmails2(List<String> emailList) {
        emails = new ArrayList<PartnerEmail>();
        for(String email : emailList) {
            emails.add(new PartnerEmail(null, email));
        }
    }
}
