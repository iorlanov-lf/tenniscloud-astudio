package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.api.protocol.MPackDynEntityConverter;
import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.db.LeagueProfileTbl;
import com.logiforge.tenniscloud.model.util.CmpUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.util.ArrayList;
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

    public static final String PLAYER_ROLE_SELF = "You";
    public static final String PLAYER_ROLE_PARTNER = "Partner";
    public static final String PLAYER_ROLE_OPPONENT1 = "Opponent 1";
    public static final String PLAYER_ROLE_OPPONENT2 = "Opponent 2";

    public enum Outcome {
        NOT_YET_PLAYED(0, "Not Yet Played"),
        COMPLETED(10, "Completed"),
        INCOMPLETE(20, "Incomplete"),
        HOME_FORFEITED(30, "Home Team Forfieted"),
        HOME_RETIRED(40, "Home Team Retired"),
        VISITOR_FORFEITED(50, "Visiting Team Forfieted"),
        VISITOR_RETIRED(60, "Visiting Team Retired");

        public static Outcome getById(int id) {
            for(Outcome outcome : values()) {
                if(outcome.getId() == id) {
                    return outcome;
                }
            }

            return null;
        }

        private final int id;
        private final String name;

        Outcome(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum MatchWeek {
        WEEK01(10, "Week 1"),
        WEEK02(20, "Week 2"),
        WEEK03(30, "Week 3"),
        WEEK04(40, "Week 4"),
        WEEK05(50, "Week 5"),
        WEEK06(60, "Week 6"),
        WEEK07(70, "Week 7"),
        WEEK08(80, "Week 8"),
        WEEK09(90, "Week 9"),
        PLAYOFF01(1000, "Playoff Round 1"),
        PLAYOFF02(2000, "Playoff Round 2"),
        PLAYOFF03(3000, "Playoff Round 3"),
        QUTERFINAL(4000, "Quaterfinal"),
        SEMIFINAL(5000, "Semifinal"),
        FINAL(6000, "Final");

        public static MatchWeek getById(int id) {
            for(MatchWeek matchWeek : values()) {
                if(matchWeek.getId() == id) {
                    return matchWeek;
                }
            }

            return null;
        }

        private final int id;
        private final String name;

        MatchWeek(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum HomeAway {
        HomeMatch(10, "Home Match"),
        AwayMatch(20, "Away Match");

        private final int id;
        private final String name;

        HomeAway(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // common attributes and relationships
    Integer matchType;
    LocalDate scheduledDt;
    LocalTime scheduledTm;
    Integer matchFormat;
    Integer outcome;
    Integer[] points;
    List<MatchPlayer> players;
    String facilityId;
    Facility facility;

    // league match attributes and relationships
    String leagueFlightId;
    LeagueFlight leagueFlight;
    Integer leagueWeek;
    LocalDate deadlineDt;

    @Override
    public String getParentId() {
        return null;
    }

    public Match() {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);
        points = new Integer[6];
        points[0] = 0;
        points[1] = 0;
        points[2] = 0;
        points[3] = 0;
        points[4] = 0;
        points[5] = 0;
    }

    public Match(String id, Long version, Integer syncState,
                 Integer matchType, LocalDate scheduledDt, LocalTime scheduledTm,
                 Integer matchFormat, Integer outcome,
                 Integer set1HomePoints, Integer set1VisitorPoints,
                 Integer set2HomePoints, Integer set2VisitorPoints,
                 Integer set3HomePoints, Integer set3VisitorPoints,
                 String facilityId,
                 String leagueFlightId, Integer leagueWeek, LocalDate deadlineDt) {
        super(id, version, syncState);

        this.matchType = matchType;
        this.scheduledDt = scheduledDt;
        this.scheduledTm = scheduledTm;

        this.matchFormat = matchFormat;
        this.outcome = outcome;
        this.points = new Integer[6];
        points[0] = set1HomePoints;
        points[1] = set1VisitorPoints;
        points[2] = set2HomePoints;
        points[3] = set2VisitorPoints;
        points[4] = set3HomePoints;
        points[5] = set3VisitorPoints;
        this.facilityId = facilityId;

        this.leagueFlightId = leagueFlightId;
        this.leagueWeek = leagueWeek;
        this.deadlineDt = deadlineDt;
    }

    public Match(Match otherMatch) {
        super(otherMatch.id, otherMatch.version, otherMatch.syncState);

        this.matchType = otherMatch.matchType;
        this.scheduledDt = otherMatch.scheduledDt;
        this.scheduledTm = otherMatch.scheduledTm;

        this.matchFormat = otherMatch.matchFormat;
        this.outcome = otherMatch.outcome;
        this.points = new Integer[6];
        points[0] = new Integer(otherMatch.points[0]);
        points[1] = new Integer(otherMatch.points[1]);
        points[2] = new Integer(otherMatch.points[2]);
        points[3] = new Integer(otherMatch.points[3]);
        points[4] = new Integer(otherMatch.points[4]);
        points[5] = new Integer(otherMatch.points[5]);
        this.facilityId = otherMatch.facilityId;

        this.leagueFlightId = otherMatch.leagueFlightId;
        this.leagueWeek = otherMatch.leagueWeek;
        this.deadlineDt = otherMatch.deadlineDt;

        if(otherMatch.players != null) {
            players = new ArrayList<MatchPlayer>();
            for(MatchPlayer otherPlayer : otherMatch.players) {
                players.add(new MatchPlayer(otherPlayer));
            }
        }

        this.facility = otherMatch.facility;
        this.leagueFlight = otherMatch.leagueFlight;
    }

    public boolean isDifferent(Match otherMatch) {
        if(!CmpUtil.eq(this.matchType, otherMatch.matchType)) {
            return true;
        }

        if(!CmpUtil.eq(this.scheduledDt, otherMatch.scheduledDt)){
            return true;
        }

        if(!CmpUtil.eq(this.scheduledTm, otherMatch.scheduledTm)){
            return true;
        }

        if(!CmpUtil.eq(this.matchFormat, otherMatch.matchFormat)){
            return true;
        }

        if(!CmpUtil.eq(this.outcome, otherMatch.outcome)){
            return true;
        }

        for(int i=0; i<6; i++) {
            if (!CmpUtil.eq(this.points[i], otherMatch.points[i])) {
                return true;
            }
        }

        if(!CmpUtil.eq(this.facilityId, otherMatch.facilityId)){
            return true;
        }

        if(!CmpUtil.eq(this.leagueWeek, otherMatch.leagueWeek)){
            return true;
        }

        if(!CmpUtil.eq(this.deadlineDt, otherMatch.deadlineDt)){
            return true;
        }

        return false;
    }

    public MatchPlayer findPlayerByUserId(String userId) {
        MatchPlayer playerToReturn = null;

        if(players != null) {
            for (MatchPlayer player : players) {
                if(player.getLeagueProfileId() != null) {
                    LeagueProfile profile = player.getLeagueProfile();
                    if(profile == null) {
                        LeagueProfileTbl profileTbl = new LeagueProfileTbl();
                        profile = (LeagueProfile)profileTbl.find(player.getLeagueProfileId());
                    }
                    if(profile.getUserId().equals(userId)) {
                        playerToReturn = player;
                        break;
                    }
                }
            }
        }

        return playerToReturn;
    }

    public MatchPlayer findPlayerByPlayerId(String playerId) {
        if(players != null) {
            for (MatchPlayer player : players) {
                if(player.id.equals(playerId)) {
                    return player;
                }
            }
        }

        return null;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public LocalDate getScheduledDt() {
        return scheduledDt;
    }

    public void setScheduledDt(LocalDate scheduledDt) {
        this.scheduledDt = scheduledDt;
    }

    public LocalTime getScheduledTm() {
        return scheduledTm;
    }

    public void setScheduledTm(LocalTime scheduledTm) {
        this.scheduledTm = scheduledTm;
    }

    public Integer getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(Integer matchFormat) {
        this.matchFormat = matchFormat;
    }

    public Integer getOutcome() {
        return outcome;
    }

    public void setOutcome(Integer outcome) {
        this.outcome = outcome;
    }

    public Integer[] getPoints() {
        return points;
    }

    public void setPoints(Integer[] points) {
        this.points = points;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<MatchPlayer> players) {
        this.players = players;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
        if(facility != null) {
            this.facilityId = facility.id;
        } else {
            this.facilityId = null;
        }
    }

    public Integer getLeagueWeek() {
        return leagueWeek;
    }

    public void setLeagueWeek(Integer leagueWeek) {
        this.leagueWeek = leagueWeek;
    }

    public LocalDate getDeadlineDt() {
        return deadlineDt;
    }

    public void setDeadlineDt(LocalDate deadlineDt) {
        this.deadlineDt = deadlineDt;
    }

    public String getLeagueFlightId() {
        return leagueFlightId;
    }

    public void setLeagueFlightId(String leagueFlightId) {
        this.leagueFlightId = leagueFlightId;
    }

    public LeagueFlight getLeagueFlight() {
        return leagueFlight;
    }

    public void setLeagueFlight(LeagueFlight leagueFlight) {
        this.leagueFlight = leagueFlight;
        setLeagueFlightId(leagueFlight.id);
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
