package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.PartnerPhone;
import com.logiforge.tenniscloud.model.TCUserPhone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class TCUserPhoneTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TCUSER_PHONE";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_PHONE_TYPE = "PHONE_TYPE";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TCUSER_PHONE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_ID TEXT," +
                    "PHONE TEXT," +
                    "PHONE_TYPE INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TCUserPhone phone = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            phone = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return phone;
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
        TCUserPhone userPhone = (TCUserPhone)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userPhone.getUserId());
        values.put(COL_PHONE, userPhone.getPhone());
        values.put(COL_PHONE_TYPE, userPhone.getPhoneType());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<TCUserPhone> findPhonesByUserId(String userId) {
        List<TCUserPhone> phones = new ArrayList<TCUserPhone>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_USER_ID+"=?",
                new String[]{userId},
                null, null, null);

        while (c.moveToNext()) {
            phones.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return phones;
    }

    private TCUserPhone fromCursor(Cursor c) {
        return new TCUserPhone(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_ID, c),
                getString(COL_PHONE, c),
                getInt(COL_PHONE_TYPE, c)
        );
    }
}
