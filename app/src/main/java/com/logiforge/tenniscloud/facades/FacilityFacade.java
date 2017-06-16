package com.logiforge.tenniscloud.facades;

import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.facade.LavoltaFacade;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.db.TCUserFacilityTbl;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.TCUserFacility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/13/2017.
 */
public class FacilityFacade {
    private static final String TAG = FacilityFacade.class.getSimpleName();

    public boolean hasFacility() {
        TCUserFacilityTbl userFacilityTbl = new TCUserFacilityTbl();
        TCUserFacade userFacade = new TCUserFacade();

        return userFacilityTbl.hasFacility(userFacade.getSelf().id);
    }

    public void createFacility(Facility facility, boolean isTCUserFacility) {
        FacilityTbl facilityTbl = new FacilityTbl();

        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_NEW_FACILITY);
            facilityTbl.uiAdd(txn, facility, null);

            if(isTCUserFacility) {
                TCUserFacilityTbl userFacilityTbl = new TCUserFacilityTbl();
                TCUserFacility userFacility = new TCUserFacility();
                TCUserFacade userFacade = new TCUserFacade();
                userFacility.setUserId(userFacade.getSelf().id);
                userFacility.setFacilityId(facility.id);
                userFacilityTbl.uiAdd(txn, userFacility, null);
            }

            db.commitTxn(txn);
        } catch(Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName():e.getMessage());
        } finally {
            db.endTxn(txn, false);
        }
    }

    public List<Facility> getFacilities() {
        List<Facility> facilities = new ArrayList<Facility>();
        TCUserFacilityTbl userFacilityTbl = new TCUserFacilityTbl();
        FacilityTbl facilityTbl = new FacilityTbl();

        TCUserFacade userFacade = new TCUserFacade();
        List<TCUserFacility> userFacilities = userFacilityTbl.getUserFacilities(userFacade.getSelf().id);
        for(TCUserFacility userFicility : userFacilities) {
            facilities.add((Facility)facilityTbl.find(userFicility.getFacilityId()));
        }

        return facilities;
    }

    public List<Facility> getFacilitiesByLikeName(String substring) {
        FacilityTbl facilityTbl = new FacilityTbl();
        return facilityTbl.findByLikeName(substring);
    }

    public Facility findFacility(String id) {
        if(id == null || id.length() == 0) {
            return null;
        } else {
            FacilityTbl facilityTbl = new FacilityTbl();
            return (Facility)facilityTbl.find(id);
        }
    }
}
