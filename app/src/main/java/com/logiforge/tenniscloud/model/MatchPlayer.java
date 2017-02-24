package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.List;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class MatchPlayer extends DynamicEntity {
    String matchId;
    Boolean isHomeTeam;
    List<MatchAvailability> availabilityList;

    // simple player
    String firstLastName;
    String email;
    String phoneNbr;

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
        super();
    }

    public MatchPlayer(String id, Long version, Integer syncState,
                       String matchId, Boolean isHomeTeam,
                       String firstLastName, String email, String phoneNbr,
                       String userId, String contactId, String leagueProfileId) {
        super(id, version, syncState);

        this.matchId = matchId;
        this.isHomeTeam = isHomeTeam;
        this.firstLastName = firstLastName;
        this.email = email;
        this.phoneNbr = phoneNbr;
        this.userId = userId;
        this.contactId = contactId;
        this.leagueProfileId = leagueProfileId;
    }
}
