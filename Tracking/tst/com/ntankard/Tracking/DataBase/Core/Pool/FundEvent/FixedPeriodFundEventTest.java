package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.UseCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new FixedPeriodFundEvent(-1, "", null, period, 1));
        assertThrows(IllegalArgumentException.class, () -> new FixedPeriodFundEvent(-1, "", category, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new FixedPeriodFundEvent(-1, "", category, period, 0));
        assertThrows(IllegalArgumentException.class, () -> new FixedPeriodFundEvent(-1, "", category, period, -1));
        assertDoesNotThrow(() -> new FixedPeriodFundEvent(-1, "", category, period, 1));
    }

    @Test
    void isActiveThisPeriod() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 7);
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Period start = period.getNext().getNext().getNext();
        Period end = start.getNext().getNext();
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category, start, 2);
        fixedPeriodFundEvent.notifyParentLink();

        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));

        UseCategoryFundTransfer useCategoryFundTransfer1 = new UseCategoryFundTransfer(-2, "", 50.0, start, fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer1.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer1.notifyParentUnLink();

        UseCategoryFundTransfer useCategoryFundTransfer2 = new UseCategoryFundTransfer(-2, "", 50.0, start.getNext(), fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer2.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer2.notifyParentUnLink();

        UseCategoryFundTransfer useCategoryFundTransfer3 = new UseCategoryFundTransfer(-2, "", 50.0, start.getLast().getLast(), fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer3.notifyParentLink();
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer3.notifyParentUnLink();

        UseCategoryFundTransfer useCategoryFundTransfer4 = new UseCategoryFundTransfer(-2, "", 50.0, start.getLast(), fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer4.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer4.notifyParentUnLink();

        UseCategoryFundTransfer useCategoryFundTransfer5 = new UseCategoryFundTransfer(-2, "", 50.0, end.getNext(), fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer5.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer5.notifyParentUnLink();

        UseCategoryFundTransfer useCategoryFundTransfer6 = new UseCategoryFundTransfer(-2, "", 50.0, end, fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer6.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast().getLast()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(start.getLast()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(start.getNext()));
        assertTrue(fixedPeriodFundEvent.isActiveThisPeriod(end));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext()));
        assertFalse(fixedPeriodFundEvent.isActiveThisPeriod(end.getNext().getNext()));
        useCategoryFundTransfer6.notifyParentUnLink();
    }

    @Test
    void isChargeThisPeriod() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 4);
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category, period.getNext(), 1);
        fixedPeriodFundEvent.notifyParentLink();

        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", 50.0, period, fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer.notifyParentLink();
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext()));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext()));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(2);
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext()));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext()));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(3);
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext()));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext()));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart(period.getNext().getNext());
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext()));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext()));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart(period.getNext().getNext().getNext());
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext()));
        assertFalse(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext()));
        assertTrue(fixedPeriodFundEvent.isChargeThisPeriod(period.getNext().getNext().getNext()));
    }

    @Test
    void getCharge() {
        assertTrue(TrackingDatabase.get().get(Period.class).size() > 4);
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category, period.getNext(), 1);
        fixedPeriodFundEvent.notifyParentLink();
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));

        double testValue = 500.0;
        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", testValue, period, fixedPeriodFundEvent, TrackingDatabase.get().getDefault(Currency.class));
        useCategoryFundTransfer.notifyParentLink();
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(2);
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-testValue / 2, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-testValue / 2, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(3);
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-testValue / 3, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart(period.getNext().getNext());
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));

        fixedPeriodFundEvent.setDuration(1);
        fixedPeriodFundEvent.setStart(period.getNext().getNext().getNext());
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext()));
        assertEquals(-0.0, fixedPeriodFundEvent.getCharge(period.getNext().getNext()));
        assertEquals(-testValue, fixedPeriodFundEvent.getCharge(period.getNext().getNext().getNext()));
    }

    @Test
    void setCategory() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category1 = TrackingDatabase.get().get(Category.class).get(0);
        Category category2 = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category1, period.getNext(), 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setCategory(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setCategory(category2));
    }

    @Test
    void setStart() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category, period.getNext(), 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(0));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setDuration(1));
    }

    @Test
    void setDuration() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = new FixedPeriodFundEvent(-1, "", category, period.getNext(), 1);
        fixedPeriodFundEvent.notifyParentLink();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setStart(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setStart(period));
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
