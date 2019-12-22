package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
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

    @Test
    void isWithin() {
        int testSize = 200;

        // Generate test data
        List<Period> periods = new ArrayList<>();
        ExistingPeriod period = new ExistingPeriod(-1, 1, 1);
        for (int i = 0; i < testSize; i++) {
            period = period.generateNext();
            periods.add(period);
        }

        for (int startIndex = 0; startIndex < testSize; startIndex++) {
            for (int endIndex = startIndex; endIndex < testSize; endIndex++) {
                Period start = periods.get(startIndex);
                int duration = endIndex - startIndex;

                for (int k = 0; k < startIndex; k++) {
                    Period toTest = periods.get(k);
                    assertFalse(toTest.isWithin(start, duration));
                }
                for (int k = startIndex; k < endIndex; k++) {
                    Period toTest = periods.get(k);
                    assertTrue(toTest.isWithin(start, duration));
                }
                for (int k = endIndex; k < testSize; k++) {
                    Period toTest = periods.get(k);
                    assertFalse(toTest.isWithin(start, duration));
                }
            }
        }
    }

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

    /**
     * Check that the order is correct
     */
    @Test
    void getOrder() {
        DataObjectTestUtil.testStandardOrder(ExistingPeriod.class);
    }
}
