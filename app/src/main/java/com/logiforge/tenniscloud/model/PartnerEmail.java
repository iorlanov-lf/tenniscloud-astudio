package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 5/5/17.
 */

public class MatchPlayerEmail extends DynamicEntity {

    String matchPlayerId;
    String email;

    @Override
    public String getParentId() {
        return matchPlayerId;
    }

    public MatchPlayerEmail() {
        super();
    }

    public MatchPlayerEmail(String id, Long version, Integer syncState,
                            String matchPlayerId, String email) {
        super(id, version, syncState);

        this.matchPlayerId = matchPlayerId;
        this.email = email;
    }

    public String getMatchPlayerId() {
        return matchPlayerId;
    }
    public void setMatchPlayerId(String matchPlayerId) {
        this.matchPlayerId = matchPlayerId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
