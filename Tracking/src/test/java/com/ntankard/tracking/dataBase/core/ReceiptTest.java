package com.ntankard.tracking.dataBase.core;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ntankard.testUtil.DataAccessUntil.getObject;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    /**
     * Load the data
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); // TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test constructor parameters
     */
    @Test
    void constructor() {
        BankTransfer bankCategoryTransfer = getObject(BankTransfer.class, 0);

        assertDoesNotThrow(() -> Receipt.make(0, "", bankCategoryTransfer));
        assertThrows(IllegalArgumentException.class, () -> Receipt.make(0, null, bankCategoryTransfer));
        assertThrows(IllegalArgumentException.class, () -> Receipt.make(0, "", null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test getFileName values
     */
    @Test
    void getFileName() {
        for (Receipt receipt : TrackingDatabase.get().get(Receipt.class)) {
            assertNotNull(receipt.getFileName());
        }
    }

    /**
     * Test getBankCategoryTransfer values
     */
    @Test
    void getBankCategoryTransfer() {
        for (Receipt receipt : TrackingDatabase.get().get(Receipt.class)) {
            assertNotNull(receipt.getBankTransfer());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(Receipt.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(Receipt.class);
    }
}
