package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Bank.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Bank bank = TrackingDatabase.get().get(Bank.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new StatementEnd(-1, null, bank, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new StatementEnd(-1, period, null, 0.0));
        assertDoesNotThrow(() -> new StatementEnd(-1, period, bank, 0.0));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

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
}
