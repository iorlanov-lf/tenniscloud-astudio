package com.logiforge.tenniscloud.model.util;

import org.joda.time.LocalTime;

/**
 * Created by iorlanov on 2/16/2017.
 */
public class LocalTimeRange {
    LocalTime startTm;
    LocalTime endTm;

    public LocalTimeRange(LocalTime startTm, LocalTime endTm) {
        this.startTm = startTm;
        this.endTm = endTm;
    }

    public LocalTimeRange(LocalTimeRange otherTmRange) {
        startTm = new LocalTime(otherTmRange.getStartTm());
        endTm = new LocalTime(otherTmRange.getEndTm());
    }

    @Override
    public String toString() {
        return startTm.toString("hh:mm a") + " - " + endTm.toString("hh:mm a");
    }

    public LocalTime getStartTm() {
        return startTm;
    }

    public LocalTime getEndTm() {
        return endTm;
    }
}
