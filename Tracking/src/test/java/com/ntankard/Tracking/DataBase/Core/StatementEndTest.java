package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ntankard.Tracking.DataBase.Core.StatementEnd.StatementEnd_End;
import static org.junit.jupiter.api.Assertions.*;

class StatementEndTest {

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
    void constructor() {
        ExistingPeriod period = DataAccessUntil.getObject(ExistingPeriod.class, 0);
        Bank bank = DataAccessUntil.getObject(Bank.class, 0);

        assertDoesNotThrow(() -> StatementEnd.make(-1, period, bank, 0.0));
        assertThrows(IllegalArgumentException.class, () -> StatementEnd.make(-2, null, bank, 0.0));
        assertThrows(IllegalArgumentException.class, () -> StatementEnd.make(-3, period, null, 0.0));
        assertThrows(IllegalArgumentException.class, () -> StatementEnd.make(-4, period, bank, null));
    }

    @Test
    void setEnd() {
        StatementEnd statementEnd = DataAccessUntil.getObject(StatementEnd.class, 0);

        assertDoesNotThrow(() -> statementEnd.set(StatementEnd_End, 0.0));
        assertThrows(IllegalArgumentException.class, () -> statementEnd.set(StatementEnd_End, null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void getPeriod() {
        for (StatementEnd statementEnd : TrackingDatabase.get().get(StatementEnd.class)) {
            assertNotNull(statementEnd.getPeriod());
        }
    }

    @Test
    void getBank() {
        for (StatementEnd statementEnd : TrackingDatabase.get().get(StatementEnd.class)) {
            assertNotNull(statementEnd.getBank());
        }
    }

    @Test
    void getEnd() {
        for (StatementEnd statementEnd : TrackingDatabase.get().get(StatementEnd.class)) {
            assertNotNull(statementEnd.getEnd());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(StatementEnd.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(StatementEnd.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}
