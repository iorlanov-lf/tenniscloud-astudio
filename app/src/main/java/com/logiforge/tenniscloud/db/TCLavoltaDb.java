package com.logiforge.tenniscloud.db;

import android.content.Context;

import com.logiforge.lavolta.android.db.DynEntityMeta;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.db.RefEntityMeta;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.MatchPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/26/2017.
 */
public class TCLavoltaDb extends LavoltaDb {
    public static final Long ACTION_NEW_LEAGUE_REGISTRATION = 10L;
    public static final Long ACTION_NEW_FACILITY = 20L;
    public static final Long ACTION_NEW_LEAGUE_MATCH = 30L;
    public static final Long ACTION_UPDATE_LEAGUE_MATCH = 40L;
    public static final Long ACTION_UNSUBSCRIBE_FROM_LEAGUE_MATCH = 50L;

    @Override
    protected List<DynEntityMeta> getDynEntityMetaData() {
        List<DynEntityMeta> metaData = new ArrayList<DynEntityMeta>();
        DynEntityMeta matchMeta = new DynEntityMeta(Match.class, MatchTbl.class, new Match.Converter(), null, true);
        metaData.add(matchMeta);
        DynEntityMeta playerMeta = new DynEntityMeta(MatchPlayer.class, MatchPlayerTbl.class, new MatchPlayer.Converter(), null, true);
        metaData.add(playerMeta);
        DynEntityMeta availabilityMeta =
                new DynEntityMeta(MatchAvailability.class, MatchAvailabilityTbl.class, new MatchAvailability.Converter(), null, true);
        metaData.add(availabilityMeta);

        return metaData;
    }

    @Override
    protected List<RefEntityMeta> getRefEntityMetaData() {
        return new ArrayList<RefEntityMeta>();
    }

    public TCLavoltaDb(Context context)
    {
        super(context, AppDb.getSqLiteDb(context));
    }
}
