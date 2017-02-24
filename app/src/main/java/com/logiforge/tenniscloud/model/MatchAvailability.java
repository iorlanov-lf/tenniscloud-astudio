package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import java.util.List;

/**
 * Created by iorlanov on 2/15/2017.
 */
public class MatchAvailability extends DynamicEntity {
    String matchPlayerId;
    LocalDateRange dateRange;
    List<LocalTimeRange> timeRanges;

    @Override
    public String getParentId() {
        return matchPlayerId;
    }

    public MatchAvailability() {
        super();
    }

    public MatchAvailability(String id, Long version, Integer syncState,
                             String matchPlayerId, LocalDateRange dateRange, List<LocalTimeRange> timeRanges) {
        super(id, version, syncState);

        this.matchPlayerId = matchPlayerId;
        this.dateRange = dateRange;
        this.timeRanges = timeRanges;
    }
}
