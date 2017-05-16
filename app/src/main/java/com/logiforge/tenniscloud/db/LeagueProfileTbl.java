package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.TCUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class LeagueProfileTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE_PROFILE";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_METRO_AREA_ID = "METRO_AREA_ID";
    public static final String COL_DISPLAY_NAME = "DISPLAY_NAME";
    public static final String COL_PHONE_NUMBER = "PHONE_NUMBER";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_PROFILE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_ID TEXT," +
                    "METRO_AREA_ID TEXT," +
                    "DISPLAY_NAME TEXT," +
                    "PHONE_NUMBER TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueProfile lp = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            lp = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lp;
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
        LeagueProfile profile = (LeagueProfile)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, profile.getUserId());
        values.put(COL_METRO_AREA_ID, profile.getLeagueMetroAreaId());
        values.put(COL_DISPLAY_NAME, profile.getDisplayName());
        values.put(COL_PHONE_NUMBER, profile.getPhoneNumber());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public LeagueProfile findByUserIdAndAreaId(String userId, String areaId) {
        LeagueProfile profile = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_USER_ID+"=?"+" AND "+COL_METRO_AREA_ID+"=?",
                new String[]{userId, areaId},
                null, null, null);

        if (c.moveToFirst()) {
            profile = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return profile;
    }

    public List<LeagueProfile> getByUserId(String userId) {
        List<LeagueProfile> profiles = new ArrayList<LeagueProfile>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_USER_ID+"=?",
                new String[]{userId},
                null, null, null);

        while (c.moveToNext()) {
            profiles.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return profiles;
    }

    private LeagueProfile fromCursor(Cursor c) {
        return new LeagueProfile(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_ID, c),
                getString(COL_METRO_AREA_ID, c),
                getString(COL_DISPLAY_NAME, c),
                getString(COL_PHONE_NUMBER, c)
        );
    }
}
