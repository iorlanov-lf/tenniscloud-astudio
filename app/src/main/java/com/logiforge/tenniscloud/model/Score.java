package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class Score extends DynamicEntity {
    String matchId;
    Integer setNbr;
    Integer homePoints;
    Integer visitorPoints;

    @Override
    public String getParentId() {
        return matchId;
    }
}
