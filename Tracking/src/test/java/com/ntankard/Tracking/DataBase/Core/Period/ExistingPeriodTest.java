package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExistingPeriodTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase();
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
        DataObjectTestUtil.testStandardParents(ExistingPeriod.class, exclude);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        List<String> exclude = new ArrayList<>();
        exclude.add("Last");
        exclude.add("Next");
        DataObjectTestUtil.checkDataObjectNotNull(ExistingPeriod.class, exclude);
    }

    /**
     * Check for imposable months
     */
    @Test
    void getMonth() {
        assertNotEquals(0, TrackingDatabase.get().get(ExistingPeriod.class).size());
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            assertTrue(period.getMonth() > 0);
            assertTrue(period.getMonth() <= 12);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}
