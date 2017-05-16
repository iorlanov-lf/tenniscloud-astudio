package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.TCUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class TCUserTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TCUSER";
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_DISPLAY_NAME = "DISPLAY_NAME";
    public static final String COL_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COL_GENDER = "GENDER";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TCUSER (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_NAME TEXT," +
                    "DISPLAY_NAME TEXT," +
                    "PHONE_NUMBER TEXT," +
                    "GENDER INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TCUser u = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            u = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return u;
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
        TCUser user = (TCUser)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.getUserName());
        values.put(COL_DISPLAY_NAME, user.getDisplayName());
        values.put(COL_PHONE_NUMBER, user.getPhoneNbr());
        values.put(COL_GENDER, user.getGender());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public TCUser getByUserName(String userName) {
        TCUser u = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_USER_NAME+"=?",
                new String[]{userName},
                null, null, null);

        if (c.moveToFirst()) {
            u = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return u;
    }

    private TCUser fromCursor(Cursor c) {
        return new TCUser(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_NAME, c),
                getString(COL_DISPLAY_NAME, c),
                getString(COL_PHONE_NUMBER, c),
                getInt(COL_GENDER, c)
        );
    }
}
