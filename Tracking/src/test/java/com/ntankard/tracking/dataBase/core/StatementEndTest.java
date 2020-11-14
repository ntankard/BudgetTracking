package com.ntankard.tracking.dataBase.core;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.ntankard.tracking.dataBase.core.StatementEnd.StatementEnd_End;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class StatementEndTest {

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
        ExistingPeriod period = DataAccessUntil.getObject(trackingDatabase, ExistingPeriod.class, 0);
        Bank bank = DataAccessUntil.getObject(trackingDatabase, Bank.class, 0);

        assertDoesNotThrow(() -> StatementEnd.make(-1, period, bank, 0.0));
        assertThrows(NullPointerException.class, () -> StatementEnd.make(-2, null, bank, 0.0));
        assertThrows(IllegalStateException.class, () -> StatementEnd.make(-3, period, null, 0.0));
        assertThrows(IllegalArgumentException.class, () -> StatementEnd.make(-4, period, bank, null));
    }

    @Test
    void setEnd() {
        StatementEnd statementEnd = DataAccessUntil.getObject(trackingDatabase, StatementEnd.class, 0);

        assertDoesNotThrow(() -> statementEnd.set(StatementEnd_End, 0.0));
        assertThrows(IllegalArgumentException.class, () -> statementEnd.set(StatementEnd_End, null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void getPeriod() {
        for (StatementEnd statementEnd : trackingDatabase.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getPeriod());
        }
    }

    @Test
    void getBank() {
        for (StatementEnd statementEnd : trackingDatabase.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getBank());
        }
    }

    @Test
    void getEnd() {
        for (StatementEnd statementEnd : trackingDatabase.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getEnd());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(trackingDatabase, StatementEnd.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(trackingDatabase, StatementEnd.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}
