package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); // TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that the values do not result in nay loss or creation of value
     */
    @Test
    void getValue() {
        assertNotEquals(0, TrackingDatabase.get().get(Transfer.class).size());
        for (Transfer transfer : TrackingDatabase.get().get(Transfer.class)) {
            if (transfer.getSourceCurrency().equals(transfer.getDestinationCurrency())) {
                assertEquals(transfer.getDestinationValue(), transfer.getSourceValue() * -1.0);
            } else {
                Double expectedTransferRate = transfer.getDestinationCurrency().getToPrimary() / transfer.getSourceCurrency().getToPrimary();
                assertTrue(transfer.getSourceValue() / transfer.getDestinationValue() < 0.0);
                Double realTransferRate = Math.abs(transfer.getSourceValue() / transfer.getDestinationValue());
                double variance = Math.abs(expectedTransferRate / realTransferRate - 1.0);
                assertTrue(variance < 0.05, "Variance:" + variance); // No more than 5% variance
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
        DataObjectTestUtil.testStandardParents(Transfer.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(Transfer.class);
    }
}
