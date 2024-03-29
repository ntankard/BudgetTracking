package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.Receipt;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
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

        assertDoesNotThrow(() -> new Receipt("", bankCategoryTransfer));
        assertThrows(NonCorruptingException.class, () -> new Receipt(null, bankCategoryTransfer));
        assertThrows(NullPointerException.class, () -> new Receipt("", null));
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
