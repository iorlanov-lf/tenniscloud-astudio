package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.model.League;


import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/21/2017.
 */
public class LeagueTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "LEAGUE";
    public static final String COL_METRO_AREA_ID = "METRO_AREA_ID";
    public static final String COL_YEAR = "YEAR";
    public static final String COL_SEASON = "SEASON";
    public static final String COL_CUSTOM_SEASON_NAME = "CUSTOM_SEASON_NAME";
    public static final String COL_TEAM_TYPE = "TEAM_TYPE";
    public static final String COL_AGE_FLIGHT = "AGE_FLIGHT";
    public static final String COL_GENDER = "GENDER";
    public static final String COL_SCHEDULING = "SCHEDULING";
    public static final String COL_LEAGUE_NAME = "LEAGUE_NAME";
    public static final String COL_REGISTRATION_END_DT = "REGISTRATION_END_DT";
    public static final String COL_PLAY_START_DT = "PLAY_START_DT";
    public static final String COL_PLAY_END_DT = "PLAY_END_DT";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "METRO_AREA_ID TEXT," +
                    "YEAR INTEGER," +
                    "SEASON INTEGER," +
                    "CUSTOM_SEASON_NAME TEXT," +
                    "TEAM_TYPE INTEGER," +
                    "AGE_FLIGHT INTEGER," +
                    "GENDER INTEGER," +
                    "SCHEDULING TEXT," +
                    "LEAGUE_NAME TEXT," +
                    "REGISTRATION_END_DT TEXT," +
                    "PLAY_START_DT TEXT," +
                    "PLAY_END_DT TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        League l = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            l = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return l;
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
        League league = (League)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_METRO_AREA_ID, league.getLeagueMetroAreaId());
        values.put(COL_YEAR, league.getYear());
        values.put(COL_SEASON, league.getSeason());
        values.put(COL_CUSTOM_SEASON_NAME, league.getCustomSeasonName());
        values.put(COL_TEAM_TYPE, league.getTeamType());
        values.put(COL_AGE_FLIGHT, league.getAgeFlight());
        values.put(COL_GENDER, league.getGender());
        values.put(COL_SCHEDULING, league.getScheduling());
        values.put(COL_LEAGUE_NAME, league.getLeagueName());
        values.put(COL_REGISTRATION_END_DT, DbUtil.toString(league.getRegistrationEndDt()));
        values.put(COL_PLAY_START_DT, DbUtil.toString(league.getPlayStartDt()));
        values.put(COL_PLAY_END_DT, DbUtil.toString(league.getPlayEndDt()));

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<League> getByMetroAreaId(String metroAreaId) {
        List<League> leagueList = new ArrayList<League>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_METRO_AREA_ID+"=?",
                new String[]{metroAreaId},
                null, null, COL_PLAY_START_DT + "," +COL_LEAGUE_NAME);

        while (c.moveToNext()) {
            League league = fromCursor(c);
            leagueList.add(league);
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return leagueList;
    }

    public List<League> getActiveLeagues(String metroAreaId) {
        List<League> allLeagues = getByMetroAreaId(metroAreaId);
        List<League> leagueList = new ArrayList<League>();
        LocalDate now = LocalDate.now();
        for(League league : allLeagues) {
            if(league.getPlayStartDt().minusDays(7).isBefore(now) && league.getPlayEndDt().plusMonths(1).isAfter(now)) {
                leagueList.add(league);
            }
        }

        return leagueList;
    }

    private League fromCursor(Cursor c) {
        return new League(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_METRO_AREA_ID, c),
                getInt(COL_YEAR, c),
                getInt(COL_SEASON, c),
                getString(COL_CUSTOM_SEASON_NAME, c),
                getInt(COL_TEAM_TYPE, c),
                getInt(COL_AGE_FLIGHT, c),
                getInt(COL_GENDER, c),
                getString(COL_SCHEDULING, c),
                getString(COL_LEAGUE_NAME, c),
                DbUtil.getLocalDate(COL_REGISTRATION_END_DT, c),
                DbUtil.getLocalDate(COL_PLAY_START_DT, c),
                DbUtil.getLocalDate(COL_PLAY_END_DT, c)
        );
    }
}
