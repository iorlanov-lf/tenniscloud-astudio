package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.TCUserFacility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class TCUserFacilityTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "TCUSER_FACILITY";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_FACILITY_ID = "FACILITY_ID";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE TCUSER_FACILITY (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "USER_ID TEXT," +
                    "FACILITY_ID TEXT" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        TCUserFacility uf = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            uf = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return uf;
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
        TCUserFacility userFacility = (TCUserFacility)dynamicEntity;

        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userFacility.getUserId());
        values.put(COL_FACILITY_ID, userFacility.getFacilityId());

        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public boolean hasFacility(String userId) {
        boolean result = false;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "USER_ID=?",
                new String[]{userId},
                null, null, null);

        if (c.moveToFirst()) {
            result = true;
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return result;
    }

    public List<TCUserFacility> getUserFacilities(String userId) {
        List<TCUserFacility> userFacilities = new ArrayList<TCUserFacility>();

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "USER_ID=?",
                new String[]{userId},
                null, null, null);

        while (c.moveToNext()) {
            TCUserFacility userFacility = fromCursor(c);
            userFacilities.add(userFacility);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return userFacilities;
    }

    private TCUserFacility fromCursor(Cursor c) {
        return new TCUserFacility(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_USER_ID, c),
                getString(COL_FACILITY_ID, c)
        );
    }
}
