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
}
