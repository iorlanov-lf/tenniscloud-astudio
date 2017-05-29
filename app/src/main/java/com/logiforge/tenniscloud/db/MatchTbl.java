package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class MatchTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "MATCH";
    public static final String COL_MATCH_TYPE = "MATCH_TYPE";
    public static final String COL_SCHEDULED_DT = "SCHEDULED_DT";
    public static final String COL_SCHEDULED_TM = "SCHEDULED_TM";
    public static final String COL_MATCH_FORMAT = "MATCH_FORMAT";
    public static final String COL_OUTCOME = "OUTCOME";
    public static final String COL_SET1_HOME_POINTS = "SET1_HOME_POINTS";
    public static final String COL_SET1_VISITOR_POINTS = "SET1_VISITOR_POINTS";
    public static final String COL_SET2_HOME_POINTS = "SET2_HOME_POINTS";
    public static final String COL_SET2_VISITOR_POINTS = "SET2_VISITOR_POINTS";
    public static final String COL_SET3_HOME_POINTS = "SET3_HOME_POINTS";
    public static final String COL_SET3_VISITOR_POINTS = "SET3_VISITOR_POINTS";
    public static final String COL_FACILITY_ID = "FACILITY_ID";
    public static final String COL_LEAGUE_FLIGHT_ID = "LEAGUE_FLIGHT_ID";
    public static final String COL_LEAGUE_WEEK = "LEAGUE_WEEK";
    public static final String COL_DEADLINE_DT = "DEADLINE_DT";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE MATCH (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_TYPE INTEGER," +
                    "SCHEDULED_DT TEXT," +
                    "SCHEDULED_TM TEXT," +
                    "MATCH_FORMAT INTEGER," +
                    "OUTCOME INTEGER," +
                    "SET1_HOME_POINTS INTEGER," +
                    "SET1_VISITOR_POINTS INTEGER," +
                    "SET2_HOME_POINTS INTEGER," +
                    "SET2_VISITOR_POINTS INTEGER," +
                    "SET3_HOME_POINTS INTEGER," +
                    "SET3_VISITOR_POINTS INTEGER," +
                    "FACILITY_ID TEXT," +
                    "LEAGUE_FLIGHT_ID TEXT," +
                    "LEAGUE_WEEK INTEGER," +
                    "DEADLINE_DT TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        Match m = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            m = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return m;
    }

    @Override
    public List<DynamicEntity> getChildren(String s) {
        return null;
    }

    @Override
    public void deleteChildren(String s) {

    }

    @Override
    public HashMap<String, InventoryItem> getInventory(String s, Long aLong) {
        return null;
    }

    @Override
    protected ContentValues getContentForInsert(DynamicEntity dynamicEntity) {
        return getContentForUpdate(dynamicEntity);
    }

    @Override
    protected ContentValues getContentForUpdate(DynamicEntity dynamicEntity) {
        Match match = (Match)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_MATCH_TYPE, match.getMatchType());
        values.put(COL_SCHEDULED_DT, DbUtil.toString(match.getScheduledDt()));
        values.put(COL_SCHEDULED_TM, DbUtil.toString(match.getScheduledTm()));
        values.put(COL_MATCH_FORMAT, match.getMatchFormat());
        values.put(COL_OUTCOME, match.getOutcome());
        values.put(COL_SET1_HOME_POINTS, match.getPoints()[0]);
        values.put(COL_SET1_VISITOR_POINTS, match.getPoints()[1]);
        values.put(COL_SET2_HOME_POINTS, match.getPoints()[2]);
        values.put(COL_SET2_VISITOR_POINTS, match.getPoints()[3]);
        values.put(COL_SET3_HOME_POINTS, match.getPoints()[4]);
        values.put(COL_SET3_VISITOR_POINTS, match.getPoints()[5]);
        values.put(COL_FACILITY_ID, match.getFacilityId());
        values.put(COL_LEAGUE_FLIGHT_ID, match.getLeagueFlightId());
        values.put(COL_LEAGUE_WEEK, match.getLeagueWeek());
        values.put(COL_DEADLINE_DT, DbUtil.toString(match.getDeadlineDt()));

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public MatchTbl() {
        super();
    }

    public MatchTbl(SQLiteDatabase db) {
        super(db);
    }

    public List<Match> getAll() {
        List<Match> matches = new ArrayList<Match>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                null, null, null, null, null);

        while (c.moveToNext()) {
            Match m = fromCursor(c);
            matches.add(m);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return matches;
    }

    private Match fromCursor(Cursor c) {
        return new Match(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getInt(COL_MATCH_TYPE, c),
                DbUtil.getLocalDate(COL_SCHEDULED_DT, c),
                DbUtil.getLocalTime(COL_SCHEDULED_TM, c),
                getInt(COL_MATCH_TYPE, c),
                getInt(COL_OUTCOME, c),
                getInt(COL_SET1_HOME_POINTS, c),
                getInt(COL_SET1_VISITOR_POINTS, c),
                getInt(COL_SET2_HOME_POINTS, c),
                getInt(COL_SET2_VISITOR_POINTS, c),
                getInt(COL_SET3_HOME_POINTS, c),
                getInt(COL_SET3_VISITOR_POINTS, c),
                getString(COL_FACILITY_ID, c),
                getString(COL_LEAGUE_FLIGHT_ID, c),
                getInt(COL_LEAGUE_WEEK, c),
                DbUtil.getLocalDate(COL_DEADLINE_DT, c)
        );
    }
}
