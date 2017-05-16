package com.logiforge.tenniscloud.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.logiforge.tenniscloud.model.Partner;

/**
 * Created by iorlanov on 2/24/2017.
 */
public class AppDb extends SQLiteOpenHelper
{
    public static final String NAME = "tennis_cloud_db";
    public static final int VERSION = 1;

    public static SQLiteDatabase sqLiteDb = null;
    public static AppDb appDb = null;

    protected Context context;

    public static SQLiteDatabase getSqLiteDb(Context context)
    {
        if(sqLiteDb == null)
        {
            appDb = new AppDb(context);
            sqLiteDb = appDb.getWritableDatabase();
        }

        return sqLiteDb;
    }

    public static AppDb getAppDb(Context context)
    {
        if(appDb == null)
        {
            appDb = new AppDb(context);
            sqLiteDb = appDb.getWritableDatabase();
        }

        return appDb;
    }

    public AppDb(Context context)
    {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(FacilityTbl.CREATE_STATEMENT);

        db.execSQL(LeagueTbl.CREATE_STATEMENT);
        db.execSQL(LeagueFlightTbl.CREATE_STATEMENT);
        db.execSQL(LeagueMetroAreaTbl.CREATE_STATEMENT);
        db.execSQL(LeagueProfileTbl.CREATE_STATEMENT);
        db.execSQL(LeagueProfileEmailTbl.CREATE_STATEMENT);
        db.execSQL(LeagueProviderTbl.CREATE_STATEMENT);
        db.execSQL(LeagueRegistrationTbl.CREATE_STATEMENT);

        db.execSQL(MatchTbl.CREATE_STATEMENT);
        db.execSQL(MatchAvailabilityTbl.CREATE_STATEMENT);
        db.execSQL(MatchPlayerTbl.CREATE_STATEMENT);
        db.execSQL(MatchPlayerEmailTbl.CREATE_STATEMENT);

        db.execSQL(PartnerTbl.CREATE_STATEMENT);
        db.execSQL(PartnerEmailTbl.CREATE_STATEMENT);
        db.execSQL(PlayingLevelTbl.CREATE_STATEMENT);

        db.execSQL(SetScoreTbl.CREATE_STATEMENT);

        db.execSQL(TCUserTbl.CREATE_STATEMENT);
        db.execSQL(TCUserEmailTbl.CREATE_STATEMENT);
        db.execSQL(TCUserFacilityTbl.CREATE_STATEMENT);
        db.execSQL(TennisContactTbl.CREATE_STATEMENT);
        db.execSQL(TennisContactEmailTbl.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
