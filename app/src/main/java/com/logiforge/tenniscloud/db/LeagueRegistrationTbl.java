package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.LeagueRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class LeagueRegistrationTbl extends DbDynamicTable{
    public static final String TABLE_NAME = "LEAGUE_REGISTRATION";
    public static final String COL_LEAGUE_PROFILE_ID = "LEAGUE_PROFILE_ID";
    public static final String COL_LEAGUE_FLIGHT_ID = "LEAGUE_FLIGHT_ID";
    public static final String COL_LEAGUE_DIVISION_ID = "LEAGUE_DIVISION_ID";
    public static final String COL_FACILITY_ID = "FACILITY_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE LEAGUE_REGISTRATION (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "LEAGUE_PROFILE_ID TEXT," +
                    "LEAGUE_FLIGHT_ID TEXT," +
                    "LEAGUE_DIVISION_ID TEXT," +
                    "FACILITY_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        LeagueRegistration lr = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            lr = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return lr;
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
        LeagueRegistration leagueReg = (LeagueRegistration)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_LEAGUE_PROFILE_ID, leagueReg.getLeagueProfileId());
        values.put(COL_LEAGUE_FLIGHT_ID, leagueReg.getLeagueFlightId());
        values.put(COL_LEAGUE_DIVISION_ID, leagueReg.getLeagueDivisionId());
        values.put(COL_FACILITY_ID, leagueReg.getFacilityId());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<LeagueRegistration> getProfileRegistrations(String profileId) {
        List<LeagueRegistration> registrations = new ArrayList<LeagueRegistration>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                COL_LEAGUE_PROFILE_ID + "=?",
                new String[]{profileId},
                null, null, null);

        while (c.moveToNext()) {
            registrations.add(fromCursor(c));
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return registrations;
    }

    private LeagueRegistration fromCursor(Cursor c) {
        return new LeagueRegistration(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_LEAGUE_PROFILE_ID, c),
                getString(COL_LEAGUE_FLIGHT_ID, c),
                getString(COL_LEAGUE_DIVISION_ID, c),
                getString(COL_FACILITY_ID, c)
        );
    }
}
