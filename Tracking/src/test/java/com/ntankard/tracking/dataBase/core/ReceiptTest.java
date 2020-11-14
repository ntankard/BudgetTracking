package com.ntankard.tracking.dataBase.core;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.ntankard.testUtil.DataAccessUntil.getObject;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class ReceiptTest {

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

    /**
     * Test constructor parameters
     */
    @Test
    void constructor() {
        BankTransfer bankCategoryTransfer = getObject(database, BankTransfer.class, 0);

        assertDoesNotThrow(() -> Receipt.make(0, "", bankCategoryTransfer));
        assertThrows(IllegalArgumentException.class, () -> Receipt.make(0, null, bankCategoryTransfer));
        assertThrows(NullPointerException.class, () -> Receipt.make(0, "", null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test getFileName values
     */
    @Test
    void getFileName() {
        for (Receipt receipt : database.get(Receipt.class)) {
            assertNotNull(receipt.getFileName());
        }
    }

    /**
     * Test getBankCategoryTransfer values
     */
    @Test
    void getBankCategoryTransfer() {
        for (Receipt receipt : database.get(Receipt.class)) {
            assertNotNull(receipt.getBankTransfer());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(database, Receipt.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(database, Receipt.class);
    }
}
