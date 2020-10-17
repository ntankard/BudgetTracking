package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.TestUtil.DataAccessUntil;
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
    void isWithin() {
        int testSize = 20;

        // Generate test data
        List<Period> periods = new ArrayList<>();
        ExistingPeriod period = ExistingPeriod.make(-1, 1, 1);
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
}