package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueProfilePhone;
import com.logiforge.tenniscloud.model.MatchPlayerPhone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class MatchPlayerPhoneTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "MATCH_PLAYER_PHONE";
    public static final String COL_MATCH_PLAYER_ID = "MATCH_PLAYER_ID";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_PHONE_TYPE = "PHONE_TYPE";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE MATCH_PLAYER_PHONE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_PLAYER_ID TEXT," +
                    "PHONE TEXT," +
                    "PHONE_TYPE INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        MatchPlayerPhone phone = null;

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
        MatchPlayerPhone playerPhone = (MatchPlayerPhone)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_MATCH_PLAYER_ID, playerPhone.getMatchPlayerId());
        values.put(COL_PHONE, playerPhone.getPhone());
        values.put(COL_PHONE_TYPE, playerPhone.getPhoneType());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<MatchPlayerPhone> findPhonesByPlayerId(String playerId) {
        List<MatchPlayerPhone> phones = new ArrayList<MatchPlayerPhone>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_MATCH_PLAYER_ID+"=?",
                new String[]{playerId},
                null, null, null);

        while (c.moveToNext()) {
            phones.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return phones;
    }

    private MatchPlayerPhone fromCursor(Cursor c) {
        return new MatchPlayerPhone(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_MATCH_PLAYER_ID, c),
                getString(COL_PHONE, c),
                getInt(COL_PHONE_TYPE, c)
        );
    }
}
