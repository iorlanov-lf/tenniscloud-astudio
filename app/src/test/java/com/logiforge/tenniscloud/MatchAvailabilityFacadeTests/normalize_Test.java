package com.logiforge.tenniscloud.MatchAvailabilityFacadeTests;

import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.EditableEntityList;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by iorlanov on 7/27/17.
 */

public class normalize_Test {

    @Test
    public void NoAvailability() {
        String inputSpecs = "";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 0);
    }

    @Test
    public void SingleAvailability() {
        String inputSpecs = "07/28/2017, 07/28/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(inputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void DateGap() {
        String inputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/30/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(inputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Adjacent_SameTmRanges() {
        String inputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Adjacent_DiffTmRanges() {
        String inputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 21:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(inputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges_SameStarts_SecondRangeIsShorter() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges_SameStarts_SecondRangeIsLonger() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges_SameStarts_SameEnds() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                "07/28/2017, 07/29/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges_SameEnds() {
        String inputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_SameTmRanges_SubDtRange() {
        String inputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00;" +
                "07/29/2017, 07/29/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_CollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 19:00, 21:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_CollapsingDtRanges2() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 21:00;" +
                "07/29/2017, 07/30/2017, 19:00, 20:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 21:00;" +
                "07/30/2017, 07/30/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/29/2017, 19:00, 20:00;" +
                "07/29/2017, 07/30/2017, 09:00, 10:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/29/2017, 09:00, 10:00, 19:00, 20:00;" +
                "07/30/2017, 07/30/2017, 09:00, 10:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 3);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_SameStarts_SecondRangeIsShorter_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/28/2017, 07/30/2017, 20:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 21:00;" +
                "07/31/2017, 07/31/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_SameStarts_SecondRangeIsLonger_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 20:00;" +
                "07/28/2017, 07/31/2017, 20:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/30/2017, 19:00, 21:00;" +
                "07/31/2017, 07/31/2017, 20:00, 21:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_SameEnds_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/29/2017, 07/31/2017, 20:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/31/2017, 19:00, 21:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 2);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_SameStarts_SameEnds() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/28/2017, 07/31/2017, 20:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 21:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_SubDtRange_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/29/2017, 07/29/2017, 20:00, 21:00";

        String expectedOutputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/29/2017, 19:00, 21:00;" +
                "07/30/2017, 07/31/2017, 19:00, 20:00";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 3);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_MultipleDtRanges_NoCollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 20:00;" +
                "07/29/2017, 08/01/2017, 21:00, 22:00;" +
                "07/30/2017, 08/02/2017, 23:00, 23:30;";

        String expectedOutputSpecs =
                "07/28/2017, 07/28/2017, 19:00, 20:00;" +
                "07/29/2017, 07/29/2017, 19:00, 20:00, 21:00, 22:00;" +
                "07/30/2017, 07/31/2017, 19:00, 20:00, 21:00, 22:00, 23:00, 23:30;" +
                "08/01/2017, 08/01/2017, 21:00, 22:00, 23:00, 23:30;" +
                "08/02/2017, 08/02/2017, 23:00, 23:30";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 5);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

    @Test
    public void Overlap_DiffTmRanges_MultipleDtRanges_CollapsingDtRanges() {
        String inputSpecs =
                "07/28/2017, 07/31/2017, 19:00, 23:30;" +
                "07/29/2017, 08/01/2017, 19:00, 22:00;" +
                "08/02/2017, 08/02/2017, 19:00, 22:00;" +
                "07/30/2017, 08/02/2017, 22:00, 23:30";

        String expectedOutputSpecs =
                "07/28/2017, 08/02/2017, 19:00, 23:30";

        EditableEntityList<MatchAvailability> editableList = Util.normalizeEditableMatchAvailabilityList(inputSpecs);

        assertTrue(editableList.getEntities().size() == 1);
        assertEquals(expectedOutputSpecs.replaceAll(" ", ""), Util.convertToString(editableList));
    }

}
