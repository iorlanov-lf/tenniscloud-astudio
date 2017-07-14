package com.logiforge.tenniscloud.facades;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.tenniscloud.db.MatchAvailabilityTbl;
import com.logiforge.tenniscloud.model.MatchAvailability;

import java.util.UUID;

/**
 * Created by iorlanov on 7/9/17.
 */

public class MatchAvailabilityFacade {
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
}
