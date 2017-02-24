package com.logiforge.tenniscloud.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.lavolta.android.model.api.sync.InventoryItem;
import com.logiforge.tenniscloud.model.SetScore;

import java.util.HashMap;
import java.util.List;

/**
 * Created by iorlanov on 2/23/2017.
 */
public class SetScoreTbl extends DbDynamicTable {
    public static final String TABLE_NAME = "SET_SCORE";
    public static final String COL_MATCH_ID = "MATCH_ID";
    public static final String COL_SET_NBR = "SET_NBR";
    public static final String COL_HOME_POINTS = "HOME_POINTS";
    public static final String COL_VISITOR_POINTS = "VISITOR_POINTS";

    public static final String CREATE_STATEMENT =
            "CREATE TABLE SET_SCORE (" +
                    "ID TEXT PRIMARY KEY," +
                    "VERSION INTEGER," +
                    "SYNC_STATE INTEGER," +
                    "MATCH_ID TEXT," +
                    "SET_NBR INTEGER," +
                    "HOME_POINTS INTEGER," +
                    "VISITOR_POINTS INTEGER" +
                    ")";

    @Override
    public DynamicEntity find(String id) {
        SetScore ss = null;

        Cursor c;
        c = db.query(TABLE_NAME, null,
                "ID=?",
                new String[]{id},
                null, null, null);

        if (c.moveToFirst()) {
            ss = fromCursor(c);
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return ss;
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
        return null;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    private SetScore fromCursor(Cursor c) {
        return new SetScore(
                getString(COL_ID, c),
                getLong(COL_VERSION, c),
                getInt(COL_SYNC_STATE, c),
                getString(COL_MATCH_ID, c),
                getInt(COL_SET_NBR, c),
                getInt(COL_HOME_POINTS, c),
                getInt(COL_VISITOR_POINTS, c)
        );
    }
}
