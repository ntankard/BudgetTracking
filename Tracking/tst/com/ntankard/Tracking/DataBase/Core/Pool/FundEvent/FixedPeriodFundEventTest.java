package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FixedPeriodFundEventTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); //TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void constructor() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        ExistingPeriod period = TrackingDatabase.get().get(ExistingPeriod.class).get(0);
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-1, "", null, period, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-1, "", solidCategory, null, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, 0));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, -1));
        assertDoesNotThrow(() -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, 1));
    }

    @Test
    void isActiveThisPeriod() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 7);
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        List<Period> periods = TrackingDatabase.get().get(Period.class);
        int start = 3;
        int end = 5;
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) periods.get(start), 2);
        fixedPeriodFundEvent.notifyParentLink();

        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));

        ManualFundTransfer useCategoryFundTransfer1 = ManualFundTransfer.make(-2, "", periods.get(start), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer1.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer1.notifyParentUnLink();

        ManualFundTransfer useCategoryFundTransfer2 = ManualFundTransfer.make(-2, "", periods.get(start + 1), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer2.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer2.notifyParentUnLink();

        ManualFundTransfer useCategoryFundTransfer3 = ManualFundTransfer.make(-2, "", periods.get(start - 2), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer3.notifyParentLink();
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer3.notifyParentUnLink();

        ManualFundTransfer useCategoryFundTransfer4 = ManualFundTransfer.make(-2, "", periods.get(start - 1), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer4.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer4.notifyParentUnLink();

        ManualFundTransfer useCategoryFundTransfer5 = ManualFundTransfer.make(-2, "", periods.get(end + 1), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer5.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer5.notifyParentUnLink();

        ManualFundTransfer useCategoryFundTransfer6 = ManualFundTransfer.make(-2, "", periods.get(end), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer6.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 2)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start - 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(start + 1)));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 1)));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(periods.get(end + 2)));
        useCategoryFundTransfer6.notifyParentUnLink();
    }

    @Test
    void isChargeThisPeriod() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 4);
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        List<Period> periods = TrackingDatabase.get().get(Period.class);
        int period = 0;
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) periods.get(period + 1), 1);
        fixedPeriodFundEvent.notifyParentLink();

        ManualFundTransfer useCategoryFundTransfer = ManualFundTransfer.make(-2, "", periods.get(period), fixedPeriodFundEvent, 50.0, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 1)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 2)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(2);
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 1)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 2)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(3);
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 1)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 2)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart((ExistingPeriod) periods.get(period + 2));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 1)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 2)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart((ExistingPeriod) periods.get(period + 3));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 1)));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 2)));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(periods.get(period + 3)));
    }

    @Test
    void getCharge() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 4);
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        List<Period> periods = TrackingDatabase.get().get(Period.class);
        int period = 0;
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) periods.get(period + 1), 1);
        fixedPeriodFundEvent.add();
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));

        double testValue = 500.0;
        ManualFundTransfer useCategoryFundTransfer = ManualFundTransfer.make(-2, "", periods.get(period), fixedPeriodFundEvent, testValue, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer.add();
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(2);
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(-testValue / 2, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-testValue / 2, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(3);
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart((ExistingPeriod) periods.get(period + 2));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart((ExistingPeriod) periods.get(period + 3));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 1)));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(periods.get(period + 2)));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(periods.get(period + 3)));
    }

    @Test
    void setCategory() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(SolidCategory.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(1);
        SolidCategory solidCategory1 = TrackingDatabase.get().get(SolidCategory.class).get(0);
        SolidCategory solidCategory2 = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory1, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setCategory(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setCategory(solidCategory2));
    }

    @Test
    void setStart() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(1);
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(0));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setDuration(1));
    }

    @Test
    void setDuration() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 3);
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(2);
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setStart(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setStart((ExistingPeriod) TrackingDatabase.get().get(Period.class).get(1)));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(FixedPeriodFundEvent.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(FixedPeriodFundEvent.class);
    }
}
