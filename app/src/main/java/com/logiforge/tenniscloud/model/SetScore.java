package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class SetScore extends DynamicEntity {
    String matchId;
    Integer setNbr;
    Integer homePoints;
    Integer visitorPoints;

    @Override
    public String getParentId() {
        return matchId;
    }

    public SetScore() {
        super();
    }

    public SetScore(String id, Long version, Integer syncState,
                    String matchId, Integer setNbr, Integer homePoints, Integer visitorPoints) {
        super(id, version, syncState);

        this.matchId = matchId;
        this.setNbr = setNbr;
        this.homePoints = homePoints;
        this.visitorPoints = visitorPoints;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Integer getSetNbr() {
        return setNbr;
    }

    public void setSetNbr(Integer setNbr) {
        this.setNbr = setNbr;
    }

    public Integer getHomePoints() {
        return homePoints;
    }

    public void setHomePoints(Integer homePoints) {
        this.homePoints = homePoints;
    }

    public Integer getVisitorPoints() {
        return visitorPoints;
    }

    public void setVisitorPoints(Integer visitorPoints) {
        this.visitorPoints = visitorPoints;
    }
}
