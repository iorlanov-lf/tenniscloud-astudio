package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

/**
 * Created by iorlanov on 2/3/2017.
 */
public class Match extends DynamicEntity {
    public static final int MATCH_TYPE_FREINDLY_MATCH = 0;
    public static final int MATCH_TYPE_LEAGUE_MATCH = 1;

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
    Integer matchType;
    LocalDate scheduledDt;
    LocalTime scheduledTm;
    Integer matchFormat;
    Integer outcome;
    List<SetScore> setScores;
    List<MatchPlayer> players;
    String facilityId;
    Facility facility;

    // league match attributes and relationships
    String leagueId;
    League league;
    Integer leagueWeek;
    LocalDate deadlineDt;

    @Override
    public String getParentId() {
        return null;
    }

    public Match() {
        super();
    }

    public Match(String id, Long version, Integer syncState,
                 Integer matchType, LocalDate scheduledDt, LocalTime scheduledTm,
                 Integer matchFormat, Integer outcome, String facilityId,
                 String leagueId, Integer leagueWeek, LocalDate deadlineDt) {
        super(id, version, syncState);

        this.matchType = matchType;
        this.scheduledDt = scheduledDt;
        this.scheduledTm = scheduledTm;

        this.matchFormat = matchFormat;
        this.outcome = outcome;
        this.facilityId = facilityId;
        this.leagueId = leagueId;
        this.leagueWeek = leagueWeek;
        this.deadlineDt = deadlineDt;
    }
}
