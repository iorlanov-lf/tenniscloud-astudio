package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueRegistration;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class LeagueRegistrationTbl extends DbDynamicTable{
    public static final String TABLE_NAME = "LEAGUE_REGISTRATION";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_LEAGUE_ID = "LEAGUE_ID";
    public static final String COL_FACILITY_ID = "FACILITY_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_REGISTRATION (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_ID TEXT," +
                    "LEAGUE_ID TEXT," +
                    "FACILITY_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueRegistration lr = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            lr = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lr;
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

    private LeagueRegistration fromCursor(Cursor c) {
        return new LeagueRegistration(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_ID, c),
                getString(COL_LEAGUE_ID, c),
                getString(COL_FACILITY_ID, c)
        );
    }
}
