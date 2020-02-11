package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.ManualBankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankCategoryTransferTest {

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

    @Test
    void constructor() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Bank.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Bank bank = TrackingDatabase.get().get(Bank.class).get(0);
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new ManualBankCategoryTransfer(-1, "", 0.0, null, bank, category));
        assertThrows(IllegalArgumentException.class, () -> new ManualBankCategoryTransfer(-1, "", 0.0, period, null, category));
        assertThrows(IllegalArgumentException.class, () -> new ManualBankCategoryTransfer(-1, "", 0.0, period, bank, null));
        assertDoesNotThrow(() -> new ManualBankCategoryTransfer(-1, "", 0.0, period, bank, category));
    }

    @Test
    void setSource() {
        assertNotEquals(0, TrackingDatabase.get().get(BankCategoryTransfer.class).size());
        BankCategoryTransfer bankCategoryTransfer = TrackingDatabase.get().get(BankCategoryTransfer.class).get(0);
        assertThrows(IllegalArgumentException.class, () -> bankCategoryTransfer.setSource(null));
    }

    @Test
    void setDestination() {
        assertNotEquals(0, TrackingDatabase.get().get(BankCategoryTransfer.class).size());
        BankCategoryTransfer bankCategoryTransfer = TrackingDatabase.get().get(BankCategoryTransfer.class).get(0);
        assertThrows(IllegalArgumentException.class, () -> bankCategoryTransfer.setDestination(null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(BankCategoryTransfer.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(BankCategoryTransfer.class);
    }
}
