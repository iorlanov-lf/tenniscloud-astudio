package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class Partner extends DynamicEntity {
    String leagueRegistrationId;

    // simple player
    String DisplayName;
    List<PartnerPhone> phones;
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
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);
    }

    public Partner(String id, Long version, Integer syncState,
                   String leagueRegistrationId,
                   String displayName,
                   String userId, String contactId, String leagueProfileId) {
        super(id, version, syncState);

        this.leagueRegistrationId = leagueRegistrationId;
        this.DisplayName = displayName;
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

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        this.DisplayName = displayName;
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

    public List<String> getEmailsAsStrings() {
        List<String> emailStrings = new ArrayList<String>();

        if(emails != null) {
            for (PartnerEmail email : emails) {
                emailStrings.add(email.getEmail());
            }
        }

        return emailStrings;
    }

    public void setEmailsFromStrings(List<String> emailList) {
        emails = new ArrayList<PartnerEmail>();
        for(String email : emailList) {
            emails.add(new PartnerEmail(null, email));
        }
    }

    public List<PartnerPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<PartnerPhone> phones) {
        this.phones = phones;
    }

    public void setPhonesFromUtilPhones(List<Phone> phoneList) {
        phones = new ArrayList<PartnerPhone>();
        for(Phone phone : phoneList) {
            phones.add(new PartnerPhone(null, phone.number, phone.type));
        }
    }
}
