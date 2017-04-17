package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/19/2017.
 */
public class LeagueProviderTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_PROVIDER";
    public static final String COL_PROVIDER_NAME = "PROVIDER_NAME";
    public static final String CREATE_STATEMENT =
        "CREATE TABLE LEAGUE_PROVIDER (" +
        "ID TEXT PRIMARY KEY," +
        "VERSION INTEGER," +
        "SYNC_STATE INTEGER," +
        "PROVIDER_NAME TEXT" +
        ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueProvider lp = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            lp = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lp;
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
        LeagueProvider provider = (LeagueProvider)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_PROVIDER_NAME, provider.getProviderName());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public LeagueProviderTbl() {
        super();
    }

    public List<LeagueProvider> getAll() {
        List<LeagueProvider> lpList = new ArrayList<LeagueProvider>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                null,
                null,
                null, null, COL_PROVIDER_NAME);

        while (c.moveToNext()) {
            LeagueProvider lp = fromCursor(c);
            lpList.add(lp);
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lpList;
    }

    private LeagueProvider fromCursor(Cursor c) {
        return new LeagueProvider(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_PROVIDER_NAME, c)
        );
    }
}
