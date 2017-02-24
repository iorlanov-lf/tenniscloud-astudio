package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.Match;

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
    public static final String COL_FACILITY_ID = "FACILITY_ID";
    public static final String COL_LEAGUE_ID = "LEAGUE_ID";
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
                    "FACILITY_ID TEXT," +
                    "LEAGUE_ID TEXT," +
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
        return null;
    }

    @Override
    protected ContentValues getContentForUpdate(DynamicEntity dynamicEntity) {
        return null;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
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
                getString(COL_FACILITY_ID, c),
                getString(COL_LEAGUE_ID, c),
                getInt(COL_LEAGUE_WEEK, c),
                DbUtil.getLocalDate(COL_DEADLINE_DT, c)
        );
    }
}
