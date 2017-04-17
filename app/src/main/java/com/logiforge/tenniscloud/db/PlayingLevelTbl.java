package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 3/10/2017.
 */
public class PlayingLevelTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "PLAYING_LEVEL";
    public static final String COL_PROVIDER_ID = "PROVIDER_ID";
    public static final String COL_ORDINAL_NUMBER = "ORDINAL_NUMBER";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String CREATE_STATEMENT =
            "CREATE TABLE PLAYING_LEVEL (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "PROVIDER_ID TEXT," +
                    "ORDINAL_NUMBER INTEGER," +
                    "DESCRIPTION TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        PlayingLevel pl = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            pl = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return pl;
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
        PlayingLevel level = (PlayingLevel)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_PROVIDER_ID, level.getProviderId());
        values.put(COL_ORDINAL_NUMBER, level.getOrdinalNumber());
        values.put(COL_DESCRIPTION, level.getDescription());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<PlayingLevel> getLevelsByProviderId(String providerId) {
        List<PlayingLevel> plList = new ArrayList<PlayingLevel>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_PROVIDER_ID+"=?",
                new String[]{providerId},
                null, null, COL_ORDINAL_NUMBER);

        while (c.moveToNext()) {
            PlayingLevel pl = fromCursor(c);
            plList.add(pl);
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return plList;
    }

    private PlayingLevel fromCursor(Cursor c) {
        return new PlayingLevel(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_PROVIDER_ID, c),
                getInt(COL_ORDINAL_NUMBER, c),
                getString(COL_DESCRIPTION, c)
        );
    }
}
