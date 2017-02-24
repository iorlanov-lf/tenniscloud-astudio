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
}
