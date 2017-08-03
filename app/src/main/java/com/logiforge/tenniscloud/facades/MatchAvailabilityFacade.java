package com.logiforge.tenniscloud.facades;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.tenniscloud.db.MatchAvailabilityTbl;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.EditableEntityList;
import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import org.joda.time.LocalDate;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by iorlanov on 7/9/17.
 */

public class MatchAvailabilityFacade {

    public static class GroupAvailability {
        public LocalDateRange dateRange;
        public List<LocalTimeRange> selfTimeRanges;
        public List<LocalTimeRange> partnerTimeRanges;
        public List<LocalTimeRange> opponent1TimeRanges;
        public List<LocalTimeRange> opponent2TimeRanges;

    }

    public MatchAvailability createMatchAvailablity(String availabilityId) {
        MatchAvailability availability = new MatchAvailability();

        if(availabilityId == null) {
            availability.syncState = DbDynamicTable.SYNC_STATE_TRANSIENT;
            availability.id = UUID.randomUUID().toString();
        } else {
            availability.syncState = DbDynamicTable.SYNC_STATE_MODIFIED;
            availability.id = availabilityId;
        }

        return availability;
    }

    public void sortByStartDate(List<MatchAvailability> availabilities) {
        Collections.sort(availabilities, new Comparator<MatchAvailability>() {
            @Override
            public int compare(MatchAvailability o1, MatchAvailability o2) {
                return o1.getDateRange().getStartDt().compareTo(o2.getDateRange().getStartDt());
            }
        });
    }

    public void normalize(EditableEntityList<MatchAvailability> editableAvailabilityList) {
        while(true) {
            List<MatchAvailability> availabilities = editableAvailabilityList.getEntities();
            sortByStartDate(availabilities);

            boolean listChanged = false;
            for(int i=0; i<availabilities.size()-1; i++) {
                MatchAvailability prevAvail = availabilities.get(i);
                MatchAvailability nextAvail = availabilities.get(i+1);
                LocalDateRange prevDtRange = prevAvail.getDateRange();
                LocalDateRange nextDtRange = nextAvail.getDateRange();

                if(nextDtRange.isAfterWithGap(prevDtRange)) {
                    continue;
                } else {
                    if(nextAvail.sameTimeRanges(prevAvail)) {
                        if(nextDtRange.getEndDt().isAfter(prevDtRange.getEndDt())) {
                            prevDtRange.setEndDt(nextDtRange.getEndDt());
                            editableAvailabilityList.markAsUpdated(prevAvail);
                        }
                        editableAvailabilityList.deleteEntity(nextAvail.id);
                    } else {
                        if(nextDtRange.isAfterNoGap(prevDtRange)) {
                            continue;
                        } else {
                            if(prevDtRange.getStartDt().equals(nextDtRange.getStartDt())) {
                                int compareResult = nextDtRange.getEndDt().compareTo(prevDtRange.getEndDt());
                                switch(compareResult) {
                                    case -1:
                                        prevDtRange.setStartDt(nextDtRange.getEndDt().plusDays(1));
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        nextAvail.mergeTimeRanges(prevAvail.getTimeRanges());
                                        editableAvailabilityList.markAsUpdated(nextAvail);
                                        break;

                                    case 0:
                                        prevAvail.mergeTimeRanges(nextAvail.getTimeRanges());
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        editableAvailabilityList.deleteEntity(nextAvail.id);
                                        break;

                                    case 1:
                                        prevAvail.mergeTimeRanges(nextAvail.getTimeRanges());
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        nextDtRange.setStartDt(prevDtRange.getEndDt().plusDays(1));
                                        editableAvailabilityList.markAsUpdated(nextAvail);
                                        break;
                                }
                            } else {
                                int compareResult = nextDtRange.getEndDt().compareTo(prevDtRange.getEndDt());
                                switch(compareResult) {
                                    case -1: {
                                        LocalDate originalPrevEndDt = prevDtRange.getEndDt();
                                        prevDtRange.setEndDt(nextDtRange.getStartDt().minusDays(1));
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        nextAvail.mergeTimeRanges(prevAvail.getTimeRanges());
                                        editableAvailabilityList.markAsUpdated(nextAvail);

                                        MatchAvailability newMatchAvailability = new MatchAvailability(prevAvail);
                                        newMatchAvailability.id = UUID.randomUUID().toString();
                                        newMatchAvailability.syncState = DbDynamicTable.SYNC_STATE_TRANSIENT;
                                        newMatchAvailability.getDateRange().setStartDt(nextDtRange.getEndDt().plusDays(1));
                                        newMatchAvailability.getDateRange().setEndDt(originalPrevEndDt);
                                        editableAvailabilityList.addEntity(newMatchAvailability);
                                        break;
                                    }

                                    case 0: {
                                        prevDtRange.setEndDt(nextDtRange.getStartDt().minusDays(1));
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        nextAvail.mergeTimeRanges(prevAvail.getTimeRanges());
                                        editableAvailabilityList.markAsUpdated(nextAvail);
                                        break;
                                    }

                                    case 1: {
                                        LocalDate origPrevEndDt = prevDtRange.getEndDt();
                                        prevDtRange.setEndDt(nextDtRange.getStartDt().minusDays(1));
                                        editableAvailabilityList.markAsUpdated(prevAvail);

                                        MatchAvailability newMatchAvailability = new MatchAvailability(nextAvail);
                                        newMatchAvailability.id = UUID.randomUUID().toString();
                                        newMatchAvailability.syncState = DbDynamicTable.SYNC_STATE_TRANSIENT;
                                        newMatchAvailability.getDateRange().setEndDt(origPrevEndDt);
                                        newMatchAvailability.mergeTimeRanges(prevAvail.getTimeRanges());
                                        editableAvailabilityList.addEntity(newMatchAvailability);

                                        nextDtRange.setStartDt(origPrevEndDt.plusDays(1));
                                        editableAvailabilityList.markAsUpdated(nextAvail);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    listChanged = true;
                    break;
                }
            }

            if(!listChanged) {
                break;
            }
        }
    }

    public Collection<GroupAvailability> getGroupAvailabilityList(
        List<MatchAvailability> selfAvailabilityList,
        List<MatchAvailability> partnerAvailabilityList,
        List<MatchAvailability> opponent1AvailabilityList,
        List<MatchAvailability> opponent2AvailabilityList
    ) {
        Map<LocalDate, GroupAvailability> groupAvailabilityMap = new TreeMap<>();

        if(selfAvailabilityList != null) {
            for (MatchAvailability availability : selfAvailabilityList) {
                List<LocalDate> dates = availability.getDateRange().getDates();
                for(LocalDate date : dates) {
                    GroupAvailability groupAvailability = new GroupAvailability();
                    groupAvailability.dateRange = new LocalDateRange(date, date);
                    groupAvailability.selfTimeRanges = availability.getTimeRanges();
                    groupAvailabilityMap.put(date, groupAvailability);
                }
            }
        }

        if(partnerAvailabilityList != null) {
            for (MatchAvailability availability : partnerAvailabilityList) {
                List<LocalDate> dates = availability.getDateRange().getDates();
                for(LocalDate date : dates) {
                    GroupAvailability groupAvailability = groupAvailabilityMap.get(date);
                    if(groupAvailability == null) {
                        groupAvailability = new GroupAvailability();
                        groupAvailability.dateRange = new LocalDateRange(date, date);
                    }
                    groupAvailability.partnerTimeRanges = availability.getTimeRanges();
                    groupAvailabilityMap.put(date, groupAvailability);
                }
            }
        }

        if(opponent1AvailabilityList != null) {
            for (MatchAvailability availability : opponent1AvailabilityList) {
                List<LocalDate> dates = availability.getDateRange().getDates();
                for(LocalDate date : dates) {
                    GroupAvailability groupAvailability = groupAvailabilityMap.get(date);
                    if(groupAvailability == null) {
                        groupAvailability = new GroupAvailability();
                        groupAvailability.dateRange = new LocalDateRange(date, date);
                    }
                    groupAvailability.opponent1TimeRanges = availability.getTimeRanges();
                    groupAvailabilityMap.put(date, groupAvailability);
                }
            }
        }

        if(opponent2AvailabilityList != null) {
            for (MatchAvailability availability : opponent2AvailabilityList) {
                List<LocalDate> dates = availability.getDateRange().getDates();
                for(LocalDate date : dates) {
                    GroupAvailability groupAvailability = groupAvailabilityMap.get(date);
                    if(groupAvailability == null) {
                        groupAvailability = new GroupAvailability();
                        groupAvailability.dateRange = new LocalDateRange(date, date);
                    }
                    groupAvailability.opponent2TimeRanges = availability.getTimeRanges();
                    groupAvailabilityMap.put(date, groupAvailability);
                }
            }
        }

        return groupAvailabilityMap.values();
    }
}
