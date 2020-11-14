package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.database.Database;
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
    private static Database database;

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        database = DataAccessUntil.getDataBase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void constructor() {
        assertNotEquals(0, database.get(Period.class).size());
        assertNotEquals(0, database.get(SolidCategory.class).size());

        ExistingPeriod period = database.get(ExistingPeriod.class).get(0);
        SolidCategory solidCategory = database.get(SolidCategory.class).get(0);

        assertThrows(NullPointerException.class, () -> FixedPeriodFundEvent.make(-1, "", null, period, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-2, "", solidCategory, null, 1));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-3, "", solidCategory, period, 0));
        assertThrows(IllegalArgumentException.class, () -> FixedPeriodFundEvent.make(-4, "", solidCategory, period, -1));
        assertDoesNotThrow(() -> FixedPeriodFundEvent.make(-1, "", solidCategory, period, 1));
    }

    @Test
    void setCategory() {
        assertNotEquals(0, database.get(Period.class).size());
        assertNotEquals(0, database.get(SolidCategory.class).size());
        assertNotEquals(1, database.get(SolidCategory.class).size());

        Period period = database.get(Period.class).get(1);
        SolidCategory solidCategory1 = database.get(SolidCategory.class).get(0);
        SolidCategory solidCategory2 = database.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory1, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setCategory(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setCategory(solidCategory2));
    }

    @Test
    void setStart() {
        assertNotEquals(0, database.get(Period.class).size());
        assertNotEquals(0, database.get(SolidCategory.class).size());

        Period period = database.get(Period.class).get(1);
        SolidCategory solidCategory = database.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(-1));
        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setDuration(0));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setDuration(1));
    }

    @Test
    void setDuration() {
        assertTrue(database.get(Period.class).size() > 3);
        assertNotEquals(0, database.get(SolidCategory.class).size());

        Period period = database.get(Period.class).get(2);
        SolidCategory solidCategory = database.get(SolidCategory.class).get(0);

        FixedPeriodFundEvent fixedPeriodFundEvent = FixedPeriodFundEvent.make(-1, "", solidCategory, (ExistingPeriod) period, 1);
        fixedPeriodFundEvent.add();

        assertThrows(IllegalArgumentException.class, () -> fixedPeriodFundEvent.setStart(null));
        assertDoesNotThrow(() -> fixedPeriodFundEvent.setStart((ExistingPeriod) database.get(Period.class).get(1)));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(database, FixedPeriodFundEvent.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(database, FixedPeriodFundEvent.class);
    }
}
