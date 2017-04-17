package com.logiforge.tenniscloud.db.util;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 2/21/2017.
 */
public class DbUtil {
    @Nullable
    static public LocalDate getLocalDate(String colName, Cursor c) {
        int colIdx = c.getColumnIndexOrThrow(colName);
        return c.isNull(colIdx)?null:LocalDate.parse(c.getString(colIdx));
    }

    @Nullable
    static public LocalTime getLocalTime(String colName, Cursor c) {
        int colIdx = c.getColumnIndexOrThrow(colName);
        return c.isNull(colIdx)?null:LocalTime.parse(c.getString(colIdx));
    }

    @Nullable
    static public LocalDateRange getLocalDateRange(String colName, Cursor c) {
        int colIdx = c.getColumnIndexOrThrow(colName);

        if(c.isNull(colIdx)) {
            return null;
        } else {
            String[] datesAsStr = c.getString(colIdx).split(",");
            return new LocalDateRange(LocalDate.parse(datesAsStr[0]), LocalDate.parse(datesAsStr[1]));
        }
    }

    @Nullable
    static public List<LocalTimeRange> getLocalTimeRanges(String colName, Cursor c) {
        int colIdx = c.getColumnIndexOrThrow(colName);

        if(c.isNull(colIdx)) {
            return null;
        } else {
            String[] timesAsStr = c.getString(colIdx).split(",");
            List<LocalTimeRange> list = new ArrayList<LocalTimeRange>();
            for(int i=0; i<timesAsStr.length-1; i=i+2) {
                list.add(new LocalTimeRange(LocalTime.parse(timesAsStr[i]), LocalTime.parse(timesAsStr[i+1])));
            }

            return list;
        }
    }

    static public String toString(LocalDate localDate) {
        if(localDate == null) {
            return null;
        } else {
            return localDate.toString();
        }
    }

    static public String toString(LocalTime localTime) {
        if(localTime == null) {
            return null;
        } else {
            return localTime.toString();
        }
    }
}
