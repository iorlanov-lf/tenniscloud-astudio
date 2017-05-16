package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueProfileEmail;
import com.logiforge.tenniscloud.model.TennisContactEmail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TennisContactEmailTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TENNIS_CONTACT_EMAIL";
    public static final String COL_TENNIS_CONTACT_ID = "TENNIS_CONTACT_ID";
    public static final String COL_EMAIL = "EMAIL";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TENNIS_CONTACT_EMAIL (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "TENNIS_CONTACT_ID TEXT," +
                    "EMAIL TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TennisContactEmail e = null;

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
        return getContentForUpdate(dynamicEntity);
    }

    @Override
    protected ContentValues getContentForUpdate(DynamicEntity dynamicEntity) {
        TennisContactEmail contactEmail = (TennisContactEmail)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_TENNIS_CONTACT_ID, contactEmail.getTennisContactId());
        values.put(COL_EMAIL, contactEmail.getEmail());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    private TennisContactEmail fromCursor(Cursor c) {
        return new TennisContactEmail(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_TENNIS_CONTACT_ID, c),
                getString(COL_EMAIL, c)
        );
    }
}
