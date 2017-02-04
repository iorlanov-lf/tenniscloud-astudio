package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by iorlanov on 2/3/2017.
 */
public class Match extends DynamicEntity {
    public static final int MATCH_FORMAT_OTHER = 0;
    public static final int MATCH_FORMAT_REGULAR_BESTOF3 = 1;
    public static final int MATCH_FORMAT_COMAN_BESTOF3 = 2;
    public static final int MATCH_FORMAT_SUPER_TIEBREAK = 3;
    public static final int MATCH_FORMAT_10GAME_SET = 4;

    public static final int MATCH_OUTCOME_NOT_YET_PLAYED = 0;
    public static final int MATCH_OUTCOME_COMPLETED = 1;
    public static final int MATCH_OUTCOME_INCOMPLETE = 2;
    public static final int MATCH_OUTCOME_HOME_FORFEIT = 3;
    public static final int MATCH_OUTCOME_HOME_RETIRED = 4;
    public static final int MATCH_OUTCOME_VISITOR_FORFEIT = 5;
    public static final int MATCH_OUTCOME_VISITOR_RETIRED = 6;

    // common attributes and relationships
    Boolean isLeagueMatch;
    Boolean isSinglesMatch;
    Date scheduledOn;
    Integer matchFormat;
    Integer outcome;
    List<Score> setScores;
    List<Player> players;

    // flex league attributes and relationships
    String seasonId;
    Season season;
    Integer leagueWeek;
    Date deadline;

    @Override
    public String getParentId() {
        return null;
    }
}
