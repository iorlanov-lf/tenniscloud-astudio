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

    public void setStartTm(LocalTime startTm) {
        this.startTm = startTm;
    }

    public LocalTime getEndTm() {
        return endTm;
    }

    public void setEndTm(LocalTime endTm) {
        this.endTm = endTm;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LocalTimeRange) {
            LocalTimeRange otherRange = (LocalTimeRange)o;
            return startTm.equals(otherRange.startTm) && endTm.equals(otherRange.endTm);
        } else {
            return false;
        }
    }
}
