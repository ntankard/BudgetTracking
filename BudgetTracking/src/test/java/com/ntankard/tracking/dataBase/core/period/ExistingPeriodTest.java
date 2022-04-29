package com.ntankard.budgetTracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
class ExistingPeriodTest {

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

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        List<String> exclude = new ArrayList<>();
        exclude.add("Last");
        exclude.add("Next");
        exclude.add("First");
        DataObjectTestUtil.testStandardParents(database, ExistingPeriod.class, exclude);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        List<String> exclude = new ArrayList<>();
        exclude.add("Last");
        exclude.add("Next");
        DataObjectTestUtil.checkDataObjectNotNull(database, ExistingPeriod.class, exclude);
    }

    /**
     * Check for imposable months
     */
    @Test
    void getMonth() {
        assertNotEquals(0, database.get(ExistingPeriod.class).size());
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            assertTrue(period.getMonth() > 0);
            assertTrue(period.getMonth() <= 12);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}
