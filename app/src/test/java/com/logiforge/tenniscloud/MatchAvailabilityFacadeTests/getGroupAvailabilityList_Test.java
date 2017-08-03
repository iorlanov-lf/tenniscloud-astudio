package com.logiforge.tenniscloud.MatchAvailabilityFacadeTests;

import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade;
import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade.GroupAvailability;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.EditableEntityList;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by iorlanov on 8/3/17.
 */

public class getGroupAvailabilityList_Test {
    MatchAvailabilityFacade facade;

    public getGroupAvailabilityList_Test() {
        facade = new MatchAvailabilityFacade();
    }

    @Test
    public void AllNulls() {
        Collection<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        null, null, null, null);

        assertEquals(groupAvailabilityCollection.size(), 0);
    }

    @Test
    public void AllEmpty() {
        Collection<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        "", "", "", "");

        assertEquals(groupAvailabilityCollection.size(), 0);
    }

    @Test
    public void OnePlayer() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String partnerSpecs = "";
        String opponent1Specs = "";
        String opponent2Specs = "";

        String expected =
                "07/28/2017, 07/28/2017, Self, 19:00, 20:00" +
                "07/29/2017, 07/29/2017, Self, 19:00, 20:00";


        Collection<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(2, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    @Test
    public void AllPlayers_SameDtRanges() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String partnerSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String opponent1Specs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String opponent2Specs = "07/28/2017, 07/29/2017, 19:00, 20:00";

        String expected =
                "07/28/2017, 07/28/2017, Self, 19:00, 20:00" +
                "                      , Prtr, 19:00, 20:00" +
                "                      , Opp1, 19:00, 20:00" +
                "                      , Opp2, 19:00, 20:00" +
                "07/29/2017, 07/29/2017, Self, 19:00, 20:00" +
                "                      , Prtr, 19:00, 20:00" +
                "                      , Opp1, 19:00, 20:00" +
                "                      , Opp2, 19:00, 20:00";

        Collection<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(2, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    private Collection<GroupAvailability> getGroupAvailability(
            String selfSpecs, String partnerSpecs, String opponent1Specs, String opponent2Specs
    ) {
        List<MatchAvailability> selfAvailability = null;
        if(selfSpecs != null)
            selfAvailability = Util.normalizeEditableMatchAvailabilityList(selfSpecs).getEntities();

        List<MatchAvailability> partnerAvailability = null;
        if(partnerSpecs != null)
            partnerAvailability = Util.normalizeEditableMatchAvailabilityList(partnerSpecs).getEntities();

        List<MatchAvailability> opponent1Availability = null;
        if(opponent1Specs != null)
            opponent1Availability = Util.normalizeEditableMatchAvailabilityList(opponent1Specs).getEntities();

        List<MatchAvailability> opponent2Availability = null;
        if(opponent2Specs != null)
            opponent2Availability = Util.normalizeEditableMatchAvailabilityList(opponent2Specs).getEntities();

        return facade.getGroupAvailabilityList(
                selfAvailability,
                partnerAvailability,
                opponent1Availability,
                opponent2Availability);
    }
}
