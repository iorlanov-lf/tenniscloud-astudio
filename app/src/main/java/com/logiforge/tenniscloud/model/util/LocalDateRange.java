package com.logiforge.tenniscloud.model.util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

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

    public List<LocalDate> getDates() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDt = startDt;
        do {
            dates.add(currentDt);
            currentDt = currentDt.plusDays(1);
        } while(currentDt.isBefore(endDt) || currentDt.isEqual(endDt));

        return dates;
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
