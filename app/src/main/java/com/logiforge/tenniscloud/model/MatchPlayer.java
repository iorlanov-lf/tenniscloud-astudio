package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.api.protocol.MPackDynEntityConverter;
import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.model.util.CmpUtil;
import com.logiforge.tenniscloud.model.util.ListDiff;
import com.logiforge.tenniscloud.model.util.Phone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class MatchPlayer extends DynamicEntity {
    String matchId;
    Boolean isHomeTeam;
    Boolean isSubscribed;
    List<MatchAvailability> availabilityList;

    // simple player
    String displayName;
    List<MatchPlayerPhone> phones;
    List<MatchPlayerEmail> emails;

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
        return matchId;
    }

    public MatchPlayer() {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);
    }

    public MatchPlayer(String id, Long version, Integer syncState,
                       String matchId, Boolean isSubscribed,
                       Boolean isHomeTeam, String displayName,
                       String userId, String contactId, String leagueProfileId) {
        super(id, version, syncState);

        this.matchId = matchId;
        this.isSubscribed = isSubscribed;
        this.isHomeTeam = isHomeTeam;
        this.displayName = displayName;
        this.userId = userId;
        this.contactId = contactId;
        this.leagueProfileId = leagueProfileId;
    }

    public MatchPlayer(String matchId, Boolean isSubscribed,
                       Boolean isHomeTeam, String displayName,
                       String userId, String contactId, String leagueProfileId) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.matchId = matchId;
        this.isSubscribed = isSubscribed;
        this.isHomeTeam = isHomeTeam;
        this.displayName = displayName;
        this.userId = userId;
        this.contactId = contactId;
        this.leagueProfileId = leagueProfileId;
    }

    public MatchPlayer(MatchPlayer otherPlayer) {
        super(otherPlayer.id, otherPlayer.version, otherPlayer.syncState);

        this.matchId = otherPlayer.matchId;
        this.isSubscribed = otherPlayer.isSubscribed;
        this.isHomeTeam = otherPlayer.isHomeTeam;
        this.displayName = otherPlayer.displayName;
        this.userId = otherPlayer.userId;
        this.contactId = otherPlayer.contactId;
        this.leagueProfileId = otherPlayer.leagueProfileId;
    }

    public boolean isDifferent(MatchPlayer otherPlayer) {

        if(!CmpUtil.eq(this.matchId, otherPlayer.matchId)) {
            return true;
        }

        if(!CmpUtil.eq(this.isSubscribed, otherPlayer.isSubscribed)) {
            return true;
        }

        if(!CmpUtil.eq(this.isHomeTeam, otherPlayer.isHomeTeam)) {
            return true;
        }

        if(!CmpUtil.eq(this.displayName, otherPlayer.displayName)) {
            return true;
        }

        if(!CmpUtil.eq(this.userId, otherPlayer.userId)) {
            return true;
        }

        if(!CmpUtil.eq(this.contactId, otherPlayer.contactId)) {
            return true;
        }

        if(!CmpUtil.eq(this.leagueProfileId, otherPlayer.leagueProfileId)) {
            return true;
        }

        return false;
    }

    public ListDiff<MatchPlayerEmail> getEmailDiff(List<MatchPlayerEmail> otherEmails) {
        ListDiff<MatchPlayerEmail> diff = new ListDiff<MatchPlayerEmail>();

        for(MatchPlayerEmail email : emails) {
            boolean found = false;
            for(MatchPlayerEmail otherEmail : otherEmails) {
                if(email.getEmail().equals(otherEmail.getEmail())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                diff.deleted.add(email);
            }
        }

        for(MatchPlayerEmail otherEmail : otherEmails) {
            boolean found = false;
            for(MatchPlayerEmail email : emails) {
                if(otherEmail.getEmail().equals(email.getEmail())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                diff.added.add(otherEmail);
            }
        }

        return diff;
    }

    public ListDiff<MatchPlayerPhone> getPhoneDiff(List<MatchPlayerPhone> otherPhones) {
        ListDiff<MatchPlayerPhone> diff = new ListDiff<MatchPlayerPhone>();

        for(MatchPlayerPhone phone : phones) {
            boolean found = false;
            boolean changed = false;
            MatchPlayerPhone updatedPhone = null;
            for(MatchPlayerPhone otherPhone : otherPhones) {
                if(phone.getPhone().equals(otherPhone.getPhone())) {
                    found = true;
                    if(!phone.getPhoneType().equals(otherPhone.getPhoneType())) {
                        changed = true;
                        updatedPhone = otherPhone;
                    }
                    break;
                }
            }

            if(!found) {
                diff.deleted.add(phone);
            } else if(changed) {
                diff.updated.add(new ListDiff.UpdatedEntity<MatchPlayerPhone>(phone, updatedPhone));
            }
        }

        for(MatchPlayerPhone otherPhone : otherPhones) {
            boolean found = false;
            for(MatchPlayerPhone phone : phones) {
                if(otherPhone.getPhone().equals(phone.getPhone())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                diff.added.add(otherPhone);
            }
        }

        return diff;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Boolean getSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        isSubscribed = subscribed;
    }

    public Boolean getHomeTeam() {
        return isHomeTeam;
    }

    public void setHomeTeam(Boolean homeTeam) {
        isHomeTeam = homeTeam;
    }

    public List<MatchAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<MatchAvailability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public List<MatchPlayerEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<MatchPlayerEmail> emails) {
        this.emails = emails;
    }

    public List<String> getEmailsAsStrings() {
        List<String> emailStrings = new ArrayList<String>();

        if(emails != null) {
            for (MatchPlayerEmail email : emails) {
                emailStrings.add(email.getEmail());
            }
        }

        return emailStrings;
    }

    public List<MatchPlayerPhone> getPhones() {
        return phones;
    }

    public void setPhones(List<MatchPlayerPhone> phones) {
        this.phones = phones;
    }

    public List<Phone> getPhonesAsUtilPhones() {
        List<Phone> utilPhones = new ArrayList<Phone>();

        if(phones != null) {
            for (MatchPlayerPhone phone : phones) {
                utilPhones.add(new Phone(phone.getPhone(), phone.getPhoneType()));
            }
        }

        return utilPhones;
    }

    public static class Converter extends MPackDynEntityConverter {

        @Override
        public void pack(Object o, Object o1) throws IOException {

        }

        @Override
        public Object unpack(Object o) throws IOException {
            return null;
        }
    }
}
