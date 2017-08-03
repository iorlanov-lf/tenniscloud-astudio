package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.api.protocol.MPackDynEntityConverter;
import com.logiforge.lavolta.android.model.DynamicEntity;
import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import org.joda.time.LocalTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public MatchAvailability(MatchAvailability otherAvailability) {
        super(otherAvailability.id, otherAvailability.version, otherAvailability.syncState);
        matchPlayerId = otherAvailability.getMatchPlayerId();
        dateRange = new LocalDateRange(otherAvailability.getDateRange());

        if(otherAvailability.getTimeRanges() != null) {
            timeRanges = new ArrayList<>();
            for(LocalTimeRange otherTmRange : otherAvailability.getTimeRanges()) {
                timeRanges.add(new LocalTimeRange(otherTmRange));
            }
        }

    }

    public void normalize() {
        Collections.sort(timeRanges, new Comparator<LocalTimeRange>() {
            @Override
            public int compare(LocalTimeRange o1, LocalTimeRange o2) {
                return o1.getStartTm().compareTo(o2.getStartTm());
            }
        });

        List<LocalTimeRange> itemsToRemove = new ArrayList<>();
        int prevIdx = 0;
        for(int i=0; i<timeRanges.size()-1; i++) {
            LocalTimeRange prevTmRange = timeRanges.get(prevIdx);
            LocalTimeRange nextTmRange = timeRanges.get(i+1);

            if(!nextTmRange.getStartTm().isAfter(prevTmRange.getEndTm())) {
                if(nextTmRange.getEndTm().isAfter(prevTmRange.getEndTm())) {
                    prevTmRange.setEndTm(nextTmRange.getEndTm());
                }
                itemsToRemove.add(nextTmRange);
            } else {
                prevIdx++;
            }
        }

        for(LocalTimeRange range : itemsToRemove) {
            timeRanges.remove(range);
        }
    }

    public boolean sameTimeRanges(MatchAvailability otherAvailability) {
        List<LocalTimeRange> otherTmRanges = otherAvailability.getTimeRanges();

        if(timeRanges.size() != otherTmRanges.size()) {
            return false;
        } else {
            for(int i =0; i<timeRanges.size(); i++) {
                if(!timeRanges.get(i).equals(otherTmRanges.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void mergeTimeRanges(List<LocalTimeRange> otherTimeRanges) {
        for(LocalTimeRange tmRange : otherTimeRanges) {
            timeRanges.add(new LocalTimeRange(tmRange));
        }
        normalize();
    }

    public String getMatchPlayerId() {
        return matchPlayerId;
    }

    public void setMatchPlayerId(String matchPlayerId) {
        this.matchPlayerId = matchPlayerId;
    }

    public LocalDateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(LocalDateRange dateRange) {
        this.dateRange = dateRange;
    }

    public List<LocalTimeRange> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(List<LocalTimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public static class Converter extends MPackDynEntityConverter {

        @Override
        public void pack(Object o, Object o1) throws IOException {

        }

        @Override
        public Object unpack(Object o) throws IOException {
            return null;
        }
    }
}
