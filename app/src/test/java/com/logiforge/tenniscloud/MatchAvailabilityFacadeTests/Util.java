package com.logiforge.tenniscloud.MatchAvailabilityFacadeTests;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade;
import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade.GroupAvailability;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.EditableEntityList;
import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by iorlanov on 8/3/17.
 */

public class Util {
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    static DateTimeFormatter dtFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
    static DateTimeFormatter tmFormatter = DateTimeFormat.forPattern(TIME_FORMAT);

    public static EditableEntityList<MatchAvailability> createEditableAvailabilityList(String specs) {
        List<MatchAvailability> availabilityList = new ArrayList<>();
        String[] availabilitySpecsArray = specs.split("[\\s]*[;][\\s]*");
        for(String availabilitySpecs : availabilitySpecsArray) {
            if(availabilitySpecs.length() > 0) {
                availabilityList.add(createAvailability(availabilitySpecs));
            }
        }
        return new EditableEntityList<MatchAvailability>(availabilityList);
    }

    public static MatchAvailability createAvailability(String specs) {
        MatchAvailability availability = new MatchAvailability();
        String[] tokens = specs.split("[\\s]*[,][\\s]*");
        assertTrue(tokens.length > 2);
        assertTrue(tokens.length % 2 == 0);

        LocalDate fromDt = dtFormatter.parseLocalDate(tokens[0]);
        LocalDate toDt = dtFormatter.parseLocalDate(tokens[1]);

        List<LocalTimeRange> dtRanges = new ArrayList<>();
        for(int i=2; i<tokens.length; i += 2) {
            LocalTime fromTm = tmFormatter.parseLocalTime(tokens[i]);
            LocalTime toTm = tmFormatter.parseLocalTime(tokens[i + 1]);
            dtRanges.add(new LocalTimeRange(fromTm, toTm));
        }

        availability.id = UUID.randomUUID().toString();
        availability.syncState = DbDynamicTable.SYNC_STATE_TRANSIENT;
        availability.setDateRange(new LocalDateRange(fromDt, toDt));
        availability.setTimeRanges(dtRanges);


        return availability;
    }

    public static String convertToString(EditableEntityList<MatchAvailability> editableList) {
        StringBuilder sb = new StringBuilder();
        for(MatchAvailability availability : editableList.getEntities()) {
            if(sb.length() > 0) {
                sb.append(";");
            }

            sb.append(convertToString(availability));
        }

        return sb.toString();
    }

    public static String convertToString(MatchAvailability availability) {
        StringBuilder sb = new StringBuilder();
        sb.append(availability.getDateRange().getStartDt().toString(DATE_FORMAT))
                .append(",")
                .append(availability.getDateRange().getEndDt().toString(DATE_FORMAT));

        for(LocalTimeRange tmRange : availability.getTimeRanges()) {
            sb.append(",")
                    .append(tmRange.getStartTm().toString(TIME_FORMAT))
                    .append(",")
                    .append(tmRange.getEndTm().toString(TIME_FORMAT));
        }

        return sb.toString();
    }

    public static String convertToString(
            Collection<GroupAvailability> groupAvailabilityCollection) {
        StringBuilder sb = new StringBuilder();

        for(GroupAvailability ga : groupAvailabilityCollection) {
            sb.append(ga.dateRange.getStartDt().toString(DATE_FORMAT))
                    .append(",")
                    .append(ga.dateRange.getEndDt().toString(DATE_FORMAT));

            if(ga.selfTimeRanges != null) {
                sb.append(",Self");
                for(LocalTimeRange tmRange : ga.selfTimeRanges) {
                    sb.append(",")
                            .append(tmRange.getStartTm().toString(TIME_FORMAT))
                            .append(",")
                            .append(tmRange.getEndTm().toString(TIME_FORMAT));
                }
            }

            if(ga.partnerTimeRanges != null) {
                sb.append(",Prtr");
                for(LocalTimeRange tmRange : ga.partnerTimeRanges) {
                    sb.append(",")
                            .append(tmRange.getStartTm().toString(TIME_FORMAT))
                            .append(",")
                            .append(tmRange.getEndTm().toString(TIME_FORMAT));
                }
            }

            if(ga.opponent1TimeRanges != null) {
                sb.append(",Opp1");
                for(LocalTimeRange tmRange : ga.opponent1TimeRanges) {
                    sb.append(",")
                            .append(tmRange.getStartTm().toString(TIME_FORMAT))
                            .append(",")
                            .append(tmRange.getEndTm().toString(TIME_FORMAT));
                }
            }

            if(ga.opponent2TimeRanges != null) {
                sb.append(",Opp2");
                for(LocalTimeRange tmRange : ga.opponent2TimeRanges) {
                    sb.append(",")
                            .append(tmRange.getStartTm().toString(TIME_FORMAT))
                            .append(",")
                            .append(tmRange.getEndTm().toString(TIME_FORMAT));
                }
            }
        }

        return sb.toString();
    }

    public static EditableEntityList<MatchAvailability> normalizeEditableMatchAvailabilityList(String inputSpecs) {
        EditableEntityList<MatchAvailability> editableList =
                createEditableAvailabilityList(inputSpecs);
        MatchAvailabilityFacade facade = new MatchAvailabilityFacade();
        facade.normalize(editableList);
        return editableList;
    }
}
