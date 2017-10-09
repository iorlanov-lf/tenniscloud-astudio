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
        List<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        null, null, null, null);

        assertEquals(groupAvailabilityCollection.size(), 0);
    }

    @Test
    public void AllEmpty() {
        List<GroupAvailability> groupAvailabilityCollection =
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
                "07/28/2017, 07/29/2017, Self, 19:00, 20:00";


        List<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(1, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    @Test
    public void OnePlayer_DateGap() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                           "07/31/2017, 08/01/2017, 19:00, 20:00";
        String partnerSpecs = "";
        String opponent1Specs = "";
        String opponent2Specs = "";

        String expected =
                "07/28/2017, 07/29/2017, Self, 19:00, 20:00" +
                "07/31/2017, 08/01/2017, Self, 19:00, 20:00";


        List<GroupAvailability> groupAvailabilityList =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(2, groupAvailabilityList.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityList));
    }

    @Test
    public void OnePlayer_NoDtGap_DiffAvail() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                           "07/30/2017, 08/01/2017, 19:00, 21:00";
        String partnerSpecs = "";
        String opponent1Specs = "";
        String opponent2Specs = "";

        String expected =
                "07/28/2017, 07/29/2017, Self, 19:00, 20:00" +
                "07/30/2017, 08/01/2017, Self, 19:00, 21:00";


        List<GroupAvailability> groupAvailabilityList =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(2, groupAvailabilityList.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityList));
    }

    @Test
    public void AllPlayers_SameDtRanges() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String partnerSpecs = "07/28/2017, 07/29/2017, 19:01, 20:00";
        String opponent1Specs = "07/28/2017, 07/29/2017, 19:02, 20:00";
        String opponent2Specs = "07/28/2017, 07/29/2017, 19:03, 20:00";

        String expected =
                "07/28/2017, 07/29/2017, Self, 19:00, 20:00" +
                "                      , Prtr, 19:01, 20:00" +
                "                      , Opp1, 19:02, 20:00" +
                "                      , Opp2, 19:03, 20:00";

        List<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(1, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    @Test
    public void AllPlayers_DiffDtRanges() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String partnerSpecs = "07/30/2017, 07/31/2017, 19:01, 20:00";
        String opponent1Specs = "08/01/2017, 08/02/2017, 19:02, 20:00";
        String opponent2Specs = "08/03/2017, 08/04/2017, 19:03, 20:00";

        String expected =
                "07/28/2017, 07/29/2017, Self, 19:00, 20:00" +
                "07/30/2017, 07/31/2017, Prtr, 19:01, 20:00" +
                "08/01/2017, 08/02/2017, Opp1, 19:02, 20:00" +
                "08/03/2017, 08/04/2017, Opp2, 19:03, 20:00";

        List<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(4, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    @Test
    public void AllPlayers_OverlapingDtRanges() {
        String selfSpecs = "07/28/2017, 07/29/2017, 19:00, 20:00";
        String partnerSpecs = "07/29/2017, 07/30/2017, 19:00, 20:00";
        String opponent1Specs = "07/30/2017, 07/31/2017, 19:00, 20:00";
        String opponent2Specs = "07/31/2017, 08/01/2017, 19:00, 20:00";

        String expected =
                "07/28/2017, 07/28/2017, Self, 19:00, 20:00" +
                "07/29/2017, 07/29/2017, Self, 19:00, 20:00" +
                "                      , Prtr, 19:00, 20:00" +
                "07/30/2017, 07/30/2017, Prtr, 19:00, 20:00" +
                "                      , Opp1, 19:00, 20:00" +
                "07/31/2017, 07/31/2017, Opp1, 19:00, 20:00" +
                "                      , Opp2, 19:00, 20:00" +
                "08/01/2017, 08/01/2017, Opp2, 19:00, 20:00";

        List<GroupAvailability> groupAvailabilityCollection =
                getGroupAvailability(
                        selfSpecs, partnerSpecs, opponent1Specs, opponent2Specs);

        assertEquals(5, groupAvailabilityCollection.size());
        assertEquals(expected.replaceAll(" ", ""), Util.convertToString(groupAvailabilityCollection));
    }

    private List<GroupAvailability> getGroupAvailability(
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
