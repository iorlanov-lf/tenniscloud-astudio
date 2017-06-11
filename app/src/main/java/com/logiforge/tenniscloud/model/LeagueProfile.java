package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class LeagueProfile extends DynamicEntity {
    String userId;
    String leagueMetroAreaId;
    LeagueMetroArea leagueMetroArea;
    String displayName;
    List<LeagueProfilePhone> phones;
    List<LeagueProfileEmail> emails;

    @Override
    public String getParentId() {
        return userId;
    }

    public LeagueProfile() {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);
    }

    public LeagueProfile(String id, Long version, Integer syncState,
                  String userId, String leagueMetroAreaId, String displayName) {
        super(id, version, syncState);

        this.userId = userId;
        this.leagueMetroAreaId = leagueMetroAreaId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLeagueMetroAreaId() {
        return leagueMetroAreaId;
    }

    public void setLeagueMetroAreaId(String leagueMetroAreaId) {
        this.leagueMetroAreaId = leagueMetroAreaId;
    }

    public LeagueMetroArea getLeagueMetroArea() {
        return leagueMetroArea;
    }

    public void setLeagueMetroArea(LeagueMetroArea leagueMetroArea) {
        this.leagueMetroArea = leagueMetroArea;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<LeagueProfileEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<LeagueProfileEmail> emails) {
        this.emails = emails;
    }

    public void setEmailsFromStrings(List<String> emailList) {
        emails = new ArrayList<LeagueProfileEmail>();
        for(String email : emailList) {
            emails.add(new LeagueProfileEmail(null, email));
        }
    }

    public List<LeagueProfilePhone> getPhones() {
        return phones;
    }

    public void setPhones(List<LeagueProfilePhone> phones) {
        this.phones = phones;
    }

    public void setPhonesFromUtilPhones(List<Phone> phoneList) {
        phones = new ArrayList<LeagueProfilePhone>();
        for(Phone phone : phoneList) {
            phones.add(new LeagueProfilePhone(null, phone.number, phone.type));
        }
    }
}
