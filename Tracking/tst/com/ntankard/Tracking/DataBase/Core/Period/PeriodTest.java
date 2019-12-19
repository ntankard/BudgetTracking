package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Integrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

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
    void generateNextAndGetFirst() {
        int month = 2;
        int year = 1;

        Period last = new ExistingPeriod(-1, month - 1, year);
        Period period = last.generateNext();
        Period first = last;

        int lastOrder = period.getOrder();
        for (int i = 0; i < 500; i++) {
            assertEquals(lastOrder++, period.getOrder());
            assertEquals(last, period.getLast());
            assertEquals(first, period.getFirst());

            assertEquals(month, period.getMonth());
            assertEquals(year, period.getYear());

            month++;
            if (month > 12) {
                month = 1;
                year++;
            }

            last = period;
            period = last.generateNext();
        }
    }

    @Test
    void isWithin() {
        int testSize = 200;

        // Generate test data
        List<Period> periods = new ArrayList<>();
        Period period = new ExistingPeriod(-1, 1, 1);
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

    @Test
    void setNext() {
        Period period1 = new ExistingPeriod(-1, 1, 1);
        Period period2 = new ExistingPeriod(-1, 2, 1);
        Period period3 = new ExistingPeriod(-1, 3, 1);

        assertThrows(IllegalArgumentException.class, () -> period1.setNext(period1));
        assertDoesNotThrow(() -> period1.setNext(period2));
        assertThrows(IllegalArgumentException.class, () -> period1.setNext(period3));
    }

    @Test
    void setLast() {
        Period period1 = new ExistingPeriod(-1, 1, 1);
        Period period2 = new ExistingPeriod(-1, 2, 1);
        Period period3 = new ExistingPeriod(-1, 3, 1);

        assertThrows(IllegalArgumentException.class, () -> period3.setLast(period1));
        assertDoesNotThrow(() -> period3.setLast(period2));
        assertThrows(IllegalArgumentException.class, () -> period3.setLast(period3));
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
        DataObjectTestUtil.testStandardParents(Period.class, exclude);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        List<String> exclude = new ArrayList<>();
        exclude.add("Last");
        exclude.add("Next");
        DataObjectTestUtil.checkDataObjectNotNull(Period.class, exclude);
    }

    /**
     * Check for imposable months
     */
    @Test
    void getMonth() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        for (Period period : TrackingDatabase.get().get(Period.class)) {
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
        DataObjectTestUtil.testStandardOrder(Period.class);
    }

    /**
     * Run the validator
     */
    @Test
    void validatePeriodSequence() {
        assertDoesNotThrow(TrackingDatabase_Integrity::validatePeriodSequence);
    }
}
