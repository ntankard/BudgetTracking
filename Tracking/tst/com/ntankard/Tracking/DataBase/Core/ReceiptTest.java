package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ntankard.TestUtil.DataAccessUntil.getObject;
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

        assertDoesNotThrow(() -> new Receipt(0, "", bankCategoryTransfer));
        assertThrows(IllegalArgumentException.class, () -> new Receipt(0, null, bankCategoryTransfer));
        assertThrows(IllegalArgumentException.class, () -> new Receipt(0, "", null));
    }

    /**
     * Test setFirstFile parameters
     */
    @Test
    void setFirstFile() {
        BankTransfer bankCategoryTransfer = getObject(BankTransfer.class, 0);

        Receipt receipt = new Receipt(0, "", bankCategoryTransfer);
        assertDoesNotThrow(() -> receipt.setFirstFile(true));
        assertThrows(IllegalArgumentException.class, () -> receipt.setFirstFile(null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test isFirstFile values
     */
    @Test
    void isFirstFile() {
        for (Receipt receipt : TrackingDatabase.get().get(Receipt.class)) {
            assertNotNull(receipt.isFirstFile());
        }
    }

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
