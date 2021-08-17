package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.javaObjectDatabase.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.ntankard.budgetTracking.dataBase.core.StatementEnd.StatementEnd_End;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class StatementEndTest {

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
        ExistingPeriod period = DataAccessUntil.getObject(database, ExistingPeriod.class, 0);
        Bank bank = DataAccessUntil.getObject(database, Bank.class, 0);

        assertDoesNotThrow(() -> new StatementEnd(period, bank, 0.0));
        assertThrows(NullPointerException.class, () -> new StatementEnd(null, bank, 0.0));
        assertThrows(NonCorruptingException.class, () -> new StatementEnd(period, null, 0.0));
        assertThrows(NonCorruptingException.class, () -> new StatementEnd(period, bank, null));
    }

    @Test
    void setEnd() {
        StatementEnd statementEnd = DataAccessUntil.getObject(database, StatementEnd.class, 0);

        assertDoesNotThrow(() -> statementEnd.set(StatementEnd_End, 0.0));
        assertThrows(NonCorruptingException.class, () -> statementEnd.set(StatementEnd_End, null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void getPeriod() {
        for (StatementEnd statementEnd : database.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getPeriod());
        }
    }

    @Test
    void getBank() {
        for (StatementEnd statementEnd : database.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getBank());
        }
    }

    @Test
    void getEnd() {
        for (StatementEnd statementEnd : database.get(StatementEnd.class)) {
            assertNotNull(statementEnd.getEnd());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(database, StatementEnd.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(database, StatementEnd.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}
