package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueProfileEmail;
import com.logiforge.tenniscloud.model.MatchPlayerEmail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class MatchPlayerEmailTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "MATCH_PLAYER_EMAIL";
    public static final String COL_MATCH_PLAYER_ID = "MATCH_PLAYER_ID";
    public static final String COL_EMAIL = "EMAIL";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TCUSER_EMAIL (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_PLAYER_ID TEXT," +
                    "EMAIL TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        MatchPlayerEmail e = null;

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
        MatchPlayerEmail playerEmail = (MatchPlayerEmail)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_MATCH_PLAYER_ID, playerEmail.getMatchPlayerId());
        values.put(COL_EMAIL, playerEmail.getEmail());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    private MatchPlayerEmail fromCursor(Cursor c) {
        return new MatchPlayerEmail(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_MATCH_PLAYER_ID, c),
                getString(COL_EMAIL, c)
        );
    }
}
