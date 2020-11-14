package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class FixedPeriodFundEventTest {

    /**
     * The database instance to use
     */
    private static TrackingDatabase trackingDatabase;

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        trackingDatabase = DataAccessUntil.getDataBase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void constructor() {
        assertNotEquals(0, trackingDatabase.get(Period.class).size());
        assertNotEquals(0, trackingDatabase.get(SolidCategory.class).size());

        ExistingPeriod period = trackingDatabase.get(ExistingPeriod.class).get(0);
        SolidCategory solidCategory = trackingDatabase.get(SolidCategory.class).get(0);

        assertThrows(NullPointerException.class, () -> FixedPeriodFundEvent.make(-1, "", null, period, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-2, "", solidCategory, null, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-3, "", solidCategory, period, 0));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-4, "", solidCategory, period, -1));
        assertDoesNotThrow(() -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, 1));
    }

    @Test
    void setCategory() {
        assertNotEquals(0, trackingDatabase.get(Period.class).size());
        assertNotEquals(0, trackingDatabase.get(SolidCategory.class).size());
        assertNotEquals(1, trackingDatabase.get(SolidCategory.class).size());

        Period period = trackingDatabase.get(Period.class).get(1);
        SolidCategory solidCategory1 = trackingDatabase.get(SolidCategory.class).get(0);
        SolidCategory solidCategory2 = trackingDatabase.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory1, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setCategory(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setCategory(solidCategory2));
    }

    @Test
    void setStart() {
        assertNotEquals(0, trackingDatabase.get(Period.class).size());
        assertNotEquals(0, trackingDatabase.get(SolidCategory.class).size());

        Period period = trackingDatabase.get(Period.class).get(1);
        SolidCategory solidCategory = trackingDatabase.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(0));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setDuration(1));
    }

    @Test
    void setDuration() {
        assertTrue(trackingDatabase.get(Period.class).size() > 3);
        assertNotEquals(0, trackingDatabase.get(SolidCategory.class).size());

        Period period = trackingDatabase.get(Period.class).get(2);
        SolidCategory solidCategory = trackingDatabase.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setStart(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setStart((ExistingPeriod) trackingDatabase.get(Period.class).get(1)));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(trackingDatabase, FixedPeriodFundEvent.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(trackingDatabase, FixedPeriodFundEvent.class);
    }
}
