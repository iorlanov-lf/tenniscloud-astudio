package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class LeagueProfileEmail extends DynamicEntity {

    String leagueProfileId;
    String email;

    @Override
    public String getParentId() {
        return leagueProfileId;
    }

    public LeagueProfileEmail() {
        super();
    }

    public LeagueProfileEmail(String leagueProfileId, String email) {
        super(null, 0L, DbDynamicTable.SYNC_STATE_ADDED);

        this.leagueProfileId = leagueProfileId;
        this.email = email;
    }

    public LeagueProfileEmail(String id, Long version, Integer syncState,
                              String leagueProfileId, String email) {
        super(id, version, syncState);

        this.leagueProfileId = leagueProfileId;
        this.email = email;
    }

    public String getLeagueProfileId() {
        return leagueProfileId;
    }
    public void setLeagueProfileId(String leagueProfileId) {
        this.leagueProfileId = leagueProfileId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
