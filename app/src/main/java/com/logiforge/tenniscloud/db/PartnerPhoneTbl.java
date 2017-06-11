package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.MatchPlayerPhone;
import com.logiforge.tenniscloud.model.PartnerPhone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class PartnerPhoneTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "PARTNER_PHONE";
    public static final String COL_PARTNER_ID = "PARTNER_ID";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_PHONE_TYPE = "PHONE_TYPE";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE PARTNER_PHONE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "PARTNER_ID TEXT," +
                    "PHONE TEXT," +
                    "PHONE_TYPE INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        PartnerPhone phone = null;

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
        PartnerPhone partnerPhone = (PartnerPhone)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_PARTNER_ID, partnerPhone.getPartnerId());
        values.put(COL_PHONE, partnerPhone.getPhone());
        values.put(COL_PHONE_TYPE, partnerPhone.getPhoneType());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<PartnerPhone> findPhonesByPartnerId(String partnerId) {
        List<PartnerPhone> phones = new ArrayList<PartnerPhone>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_PARTNER_ID+"=?",
                new String[]{partnerId},
                null, null, null);

        while (c.moveToNext()) {
            phones.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return phones;
    }

    private PartnerPhone fromCursor(Cursor c) {
        return new PartnerPhone(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_PARTNER_ID, c),
                getString(COL_PHONE, c),
                getInt(COL_PHONE_TYPE, c)
        );
    }
}
