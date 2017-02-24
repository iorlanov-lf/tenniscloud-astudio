package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.MatchAvailability;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class MatchAvailabilityTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "MATCH_AVAILABILITY";
    public static final String COL_MATCH_PLAYER_ID = "MATCH_PLAYER_ID";
    public static final String COL_DATE_RANGE = "DATE_RANGE";
    public static final String COL_TIME_RANGES = "TIME_RANGES";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE MATCH_AVAILABILITY (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_PLAYER_ID TEXT," +
                    "DATE_RANGE TEXT," +
                    "TIME_RANGES TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        MatchAvailability ma = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            ma = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return ma;
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

    private MatchAvailability fromCursor(Cursor c) {
        return new MatchAvailability(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_MATCH_PLAYER_ID, c),
                DbUtil.getLocalDateRange(COL_DATE_RANGE, c),
                DbUtil.getLocalTimeRanges(COL_TIME_RANGES, c)
        );
    }
}
