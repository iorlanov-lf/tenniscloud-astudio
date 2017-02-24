package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/20/2017.
 */
public class LeagueMetroAreaTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_METRO_AREA";
    public static final String COL_PROVIDER_ID = "PROVIDER_ID";
    public static final String COL_STATE = "STATE";
    public static final String COL_METRO_AREA_NAME = "METRO_AREA_NAME";
    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_METRO_AREA (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "PROVIDER_ID TEXT," +
                    "STATE TEXT," +
                    "METRO_AREA_NAME TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueMetroArea ma = null;

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

    public List<LeagueMetroArea> getByProviderId(String providerId) {
        List<LeagueMetroArea> maList = new ArrayList<LeagueMetroArea>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_PROVIDER_ID+"=?",
                new String[]{providerId},
                null, null, COL_METRO_AREA_NAME);

        while (c.moveToNext()) {
            LeagueMetroArea ma = fromCursor(c);
            maList.add(ma);
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return maList;
    }

    private LeagueMetroArea fromCursor(Cursor c) {
        return new LeagueMetroArea(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_PROVIDER_ID, c),
                getString(COL_STATE, c),
                getString(COL_METRO_AREA_NAME, c)
        );
    }
}
