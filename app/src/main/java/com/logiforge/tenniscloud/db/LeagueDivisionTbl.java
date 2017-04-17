package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueDivision;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/25/2017.
 */
public class LeagueDivisionTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_DIVISION";
    public static final String COL_LEAGUE_FLIGHT_ID = "LEAGUE_FLIGHT_ID";
    public static final String COL_PROVIDER_ASSIGNED_ID = "PROVIDER_ASSIGNED_ID";
    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_DIVISION (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "LEAGUE_FLIGHT_ID TEXT," +
                    "PROVIDER_ASSIGNED_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueDivision ld = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            ld = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return ld;
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
        LeagueDivision leagueDivision = (LeagueDivision)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_LEAGUE_FLIGHT_ID, leagueDivision.getLeagueFlightId());
        values.put(COL_PROVIDER_ASSIGNED_ID, leagueDivision.getProviderAssignedId());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    private LeagueDivision fromCursor(Cursor c) {
        return new LeagueDivision (
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_LEAGUE_FLIGHT_ID, c),
                getString(COL_PROVIDER_ASSIGNED_ID, c)
        );
    }
}
