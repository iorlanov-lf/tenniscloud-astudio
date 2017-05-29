package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.MatchPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class MatchPlayerTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "MATCH_PLAYER";
    public static final String COL_MATCH_ID = "MATCH_ID";
    public static final String COL_IS_HOME_TEAM = "IS_HOME_TEAM";
    public static final String COL_FIRST_LAST_NAME = "FIRST_LAST_NAME";
    public static final String COL_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_CONTACT_ID = "CONTACT_ID";
    public static final String COL_LEAGUE_PROFILE_ID = "LEAGUE_PROFILE_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE MATCH_PLAYER (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_ID TEXT," +
                    "IS_HOME_TEAM INTEGER," +
                    "FIRST_LAST_NAME TEXT," +
                    "PHONE_NUMBER TEXT," +
                    "USER_ID TEXT," +
                    "CONTACT_ID TEXT," +
                    "LEAGUE_PROFILE_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        MatchPlayer mp = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            mp = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return mp;
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
        MatchPlayer matchPlayer = (MatchPlayer)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_MATCH_ID, matchPlayer.getMatchId());
        values.put(COL_IS_HOME_TEAM, DbUtil.toInteger(matchPlayer.getHomeTeam()));
        values.put(COL_FIRST_LAST_NAME, matchPlayer.getFirstLastName());
        values.put(COL_PHONE_NUMBER, matchPlayer.getPhoneNbr());
        values.put(COL_USER_ID, matchPlayer.getUserId());
        values.put(COL_CONTACT_ID, matchPlayer.getContactId());
        values.put(COL_LEAGUE_PROFILE_ID, matchPlayer.getLeagueProfileId());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public MatchPlayerTbl() {
        super();
    }

    public MatchPlayerTbl(SQLiteDatabase db) {
        super(db);
    }

    public List<MatchPlayer> findPlayersByMatchId(String matchId) {
        List<MatchPlayer> players = new ArrayList<MatchPlayer>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_MATCH_ID + "=?",
                new String[]{matchId},
                null, null, null);

        while (c.moveToNext()) {
            players.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return players;
    }

    private MatchPlayer fromCursor(Cursor c) {
        return new MatchPlayer(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_MATCH_ID, c),
                getBoolean(COL_IS_HOME_TEAM, c),
                getString(COL_FIRST_LAST_NAME, c),
                getString(COL_PHONE_NUMBER, c),
                getString(COL_USER_ID, c),
                getString(COL_CONTACT_ID, c),
                getString(COL_LEAGUE_PROFILE_ID, c)
        );
    }
}
