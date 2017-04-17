package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueFlight;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 3/2/2017.
 */
public class LeagueFlightTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_FLIGHT";
    public static final String COL_LEAGUE_ID = "LEAGUE_ID";
    public static final String COL_PLAYING_LEVEL_ID = "PLAYING_LEVEL_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_FLIGHT (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "LEAGUE_ID TEXT," +
                    "PLAYING_LEVEL_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueFlight lf = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            lf = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lf;
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
        LeagueFlight leagueFlight = (LeagueFlight)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_LEAGUE_ID, leagueFlight.getLeagueId());
        values.put(COL_PLAYING_LEVEL_ID, leagueFlight.getPlayingLevelId());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public LeagueFlight findByLeagueAndLevel(String leagueId, String levelId) {
        LeagueFlight lf = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "LEAGUE_ID=? AND PLAYING_LEVEL_ID=?",
                new String[]{leagueId, levelId},
                null, null, null);

        if (c.moveToFirst()) {
            lf = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lf;
    }

    private LeagueFlight fromCursor(Cursor c) {
        return new LeagueFlight(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_LEAGUE_ID, c),
                getString(COL_PLAYING_LEVEL_ID, c)
        );
    }
}
