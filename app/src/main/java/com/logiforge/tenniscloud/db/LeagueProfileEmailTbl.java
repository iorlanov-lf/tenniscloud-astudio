package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.LeagueProfileEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 5/5/17.
 */

public class LeagueProfileEmailTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_PROFILE_EMAIL";
    public static final String COL_LEAGUE_PROFILE_ID = "LEAGUE_PROFILE_ID";
    public static final String COL_EMAIL = "EMAIL";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_PROFILE_EMAIL (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "LEAGUE_PROFILE_ID TEXT," +
                    "EMAIL TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueProfileEmail e = null;

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
        LeagueProfileEmail profileEmail = (LeagueProfileEmail)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_LEAGUE_PROFILE_ID, profileEmail.getLeagueProfileId());
        values.put(COL_EMAIL, profileEmail.getEmail());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<LeagueProfileEmail> findEmailsByProfileId(String partnerId) {
        List<LeagueProfileEmail> emails = new ArrayList<LeagueProfileEmail>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_LEAGUE_PROFILE_ID+"=?",
                new String[]{partnerId},
                null, null, null);

        while (c.moveToNext()) {
            emails.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return emails;
    }

    private LeagueProfileEmail fromCursor(Cursor c) {
        return new LeagueProfileEmail(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_LEAGUE_PROFILE_ID, c),
                getString(COL_EMAIL, c)
        );
    }
}
