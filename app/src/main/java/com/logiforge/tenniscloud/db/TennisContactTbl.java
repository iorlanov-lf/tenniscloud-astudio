package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.TennisContact;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class TennisContactTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TENNIS_CONTACT";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_FIRST_LAST_NAME = "FIRST_LAST_NAME";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COL_OTHER_USER_ID = "OTHER_USER_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TENNIS_CONTACT (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_ID TEXT," +
                    "FIRST_LAST_NAME TEXT," +
                    "EMAIL TEXT," +
                    "PHONE_NUMBER TEXT," +
                    "OTHER_USER_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TennisContact tc = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            tc = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return tc;
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

    private TennisContact fromCursor(Cursor c) {
        return new TennisContact(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_ID, c),
                getString(COL_FIRST_LAST_NAME, c),
                getString(COL_EMAIL, c),
                getString(COL_PHONE_NUMBER, c),
                getString(COL_OTHER_USER_ID, c)
        );
    }
}
