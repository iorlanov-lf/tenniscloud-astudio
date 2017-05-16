package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.TCUserEmail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TCUserEmailTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TCUSER_EMAIL";
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_CONFIRMED = "CONFIRMED";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TCUSER_EMAIL (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_NAME TEXT," +
                    "EMAIL TEXT," +
                    "CONFIRMED INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TCUserEmail e = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            e = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return e;
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
        TCUserEmail userEmail = (TCUserEmail)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, userEmail.getUserName());
        values.put(COL_EMAIL, userEmail.getEmail());
        values.put(COL_CONFIRMED, DbUtil.toInteger(userEmail.getConfirmed()));

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    private TCUserEmail fromCursor(Cursor c) {
        return new TCUserEmail(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_NAME, c),
                getString(COL_EMAIL, c),
                getBoolean(COL_CONFIRMED, c)
        );
    }
}
