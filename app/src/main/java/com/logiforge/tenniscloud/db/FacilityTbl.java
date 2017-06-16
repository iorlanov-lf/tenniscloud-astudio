package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.Facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class FacilityTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "FACILITY";
    public static final String COL_NAME = "NAME";
    public static final String COL_STREET_ADDRESS = "STREET_ADDRESS";
    public static final String COL_CITY = "CITY";
    public static final String COL_STATE = "STATE";
    public static final String COL_ZIP = "ZIP";
    public static final String COL_DIRECTIONS = "DIRECTIONS";
    public static final String COL_IS_REFERENCE = "IS_REFERENCE";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE FACILITY (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "NAME TEXT," +
                    "STREET_ADDRESS TEXT," +
                    "CITY TEXT," +
                    "STATE TEXT," +
                    "ZIP TEXT," +
                    "DIRECTIONS TEXT," +
                    "IS_REFERENCE INTEGER" +
                    ")";


    @Override
    public DynamicEntity find(String id) {
        Facility f = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            f = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return f;
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
        Facility facility = (Facility)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_NAME, facility.getName());
        values.put(COL_STREET_ADDRESS, facility.getStreetAddress());
        values.put(COL_CITY, facility.getCity());
        values.put(COL_STATE, facility.getState());
        values.put(COL_ZIP, facility.getZip());
        values.put(COL_DIRECTIONS, facility.getDirections());
        values.put(COL_IS_REFERENCE, facility.getReferenceEntity()?1:0);

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public List<Facility> findByLikeName(String substring) {
        List<Facility> facilities = new ArrayList<>();

        Cursor c = db.rawQuery(
                "select * from " + TABLE_NAME + " where " + COL_NAME + " like '%" + substring + "%'", null);
        while (c.moveToNext()) {
            Facility f = fromCursor(c);
            facilities.add(f);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return facilities;
    }

    private Facility fromCursor(Cursor c) {
        return new Facility(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_NAME, c),
                getString(COL_STREET_ADDRESS, c),
                getString(COL_CITY, c),
                getString(COL_STATE, c),
                getString(COL_ZIP, c),
                getString(COL_DIRECTIONS, c),
                getBoolean(COL_IS_REFERENCE, c)
        );
    }
}
