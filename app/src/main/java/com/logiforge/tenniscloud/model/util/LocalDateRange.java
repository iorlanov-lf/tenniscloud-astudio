package com.logiforge.tenniscloud.model.util;

import org.joda.time.LocalDate;

/**
 * Created by iorlanov on 2/16/2017.
 */
public class LocalDateRange {
    LocalDate startDt;
    LocalDate endDt;

    public LocalDateRange(LocalDate startDt, LocalDate endDt) {
        this.startDt = startDt;
        this.endDt = endDt;
    }

    public LocalDateRange(LocalDateRange otherDateRange) {
        startDt = new LocalDate(otherDateRange.getStartDt());
        endDt = new LocalDate(otherDateRange.getEndDt());
    }

    @Override
    public String toString() {
        return startDt.toString("MM/dd") + " - " + endDt.toString("MM/dd");
    }

    public LocalDate getStartDt() {
        return startDt;
    }

    public LocalDate getEndDt() {
        return endDt;
    }
}
