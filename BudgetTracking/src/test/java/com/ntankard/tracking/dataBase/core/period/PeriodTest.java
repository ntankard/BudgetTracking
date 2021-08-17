package com.ntankard.budgetTracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.testUtil.DataAccessUntil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class PeriodTest {

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
    void isWithin() {
        int testSize = 20;

        // Generate test data
        List<Period> periods = new ArrayList<>();
        ExistingPeriod period = new ExistingPeriod(database, -1, 1, 1);
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