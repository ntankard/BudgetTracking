package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
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
        assertNotEquals(0, TrackingDatabase.get().get(SolidCategory.class).size());

        ExistingPeriod period = TrackingDatabase.get().get(ExistingPeriod.class).get(0);
        SolidCategory solidCategory = TrackingDatabase.get().get(SolidCategory.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-1, "", null, period, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-2, "", solidCategory, null, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-3, "", solidCategory, period, 0));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-4, "", solidCategory, period, -1));
        assertDoesNotThrow(() -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, 1));
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
        fixedPeriodFundEvent.add();

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
        fixedPeriodFundEvent.add();

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
        fixedPeriodFundEvent.add();

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
