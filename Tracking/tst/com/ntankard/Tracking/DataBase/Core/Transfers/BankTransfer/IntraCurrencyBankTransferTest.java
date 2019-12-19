package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Integrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntraCurrencyBankTransferTest {

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
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Currency currency1 = TrackingDatabase.get().get(Currency.class).get(0);
        Currency currency2 = TrackingDatabase.get().get(Currency.class).get(1);

        assertNotEquals(0, currency1.getChildren(Bank.class).size());
        assertNotEquals(1, currency1.getChildren(Bank.class).size());
        assertNotEquals(0, currency2.getChildren(Bank.class).size());

        Bank currency1_bank1 = currency1.getChildren(Bank.class).get(0);
        Bank currency1_bank2 = currency1.getChildren(Bank.class).get(1);
        Bank currency2_bank1 = currency2.getChildren(Bank.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, null, currency1_bank1, currency2_bank1));
        assertThrows(IllegalArgumentException.class, () -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, null, currency2_bank1));
        assertThrows(IllegalArgumentException.class, () -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, null));
        assertThrows(IllegalArgumentException.class, () -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, currency1_bank1));
        assertThrows(IllegalArgumentException.class, () -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, currency1_bank2));
        assertDoesNotThrow(() -> new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, currency2_bank1));
    }

    @Test
    void setSource() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Currency currency1 = TrackingDatabase.get().get(Currency.class).get(0);
        Currency currency2 = TrackingDatabase.get().get(Currency.class).get(1);

        assertNotEquals(0, currency1.getChildren(Bank.class).size());
        assertNotEquals(1, currency1.getChildren(Bank.class).size());
        assertNotEquals(2, currency1.getChildren(Bank.class).size());
        assertNotEquals(0, currency2.getChildren(Bank.class).size());

        Bank currency1_bank1 = currency1.getChildren(Bank.class).get(0);
        Bank currency1_bank2 = currency1.getChildren(Bank.class).get(1);
        Bank currency1_bank3 = currency1.getChildren(Bank.class).get(2);
        Bank currency2_bank1 = currency2.getChildren(Bank.class).get(0);

        IntraCurrencyBankTransfer intraCurrencyBankTransfer = new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, currency2_bank1);
        intraCurrencyBankTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> intraCurrencyBankTransfer.setSource(null));

        intraCurrencyBankTransfer.setSource(currency1_bank3);
        assertEquals(currency2_bank1, intraCurrencyBankTransfer.getDestination());

        intraCurrencyBankTransfer.setSource(currency1_bank2);
        assertEquals(currency2_bank1, intraCurrencyBankTransfer.getDestination());

        intraCurrencyBankTransfer.setSource(currency2_bank1);
        assertNotEquals(currency2_bank1, intraCurrencyBankTransfer.getDestination());
        assertNotEquals(currency2, intraCurrencyBankTransfer.getDestination().getCurrency());
    }

    @Test
    void setDestination() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Currency currency1 = TrackingDatabase.get().get(Currency.class).get(0);
        Currency currency2 = TrackingDatabase.get().get(Currency.class).get(1);

        assertNotEquals(0, currency1.getChildren(Bank.class).size());
        assertNotEquals(1, currency1.getChildren(Bank.class).size());
        assertNotEquals(0, currency2.getChildren(Bank.class).size());
        assertNotEquals(1, currency2.getChildren(Bank.class).size());

        Bank currency1_bank1 = currency1.getChildren(Bank.class).get(0);
        Bank currency1_bank2 = currency1.getChildren(Bank.class).get(1);
        Bank currency2_bank1 = currency2.getChildren(Bank.class).get(0);
        Bank currency2_bank2 = currency2.getChildren(Bank.class).get(1);

        IntraCurrencyBankTransfer intraCurrencyBankTransfer = new IntraCurrencyBankTransfer(-1, "", 0.0, 0.0, period, currency1_bank1, currency2_bank1);
        intraCurrencyBankTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> intraCurrencyBankTransfer.setDestination(null));
        assertThrows(IllegalArgumentException.class, () -> intraCurrencyBankTransfer.setDestination(currency1_bank1));
        assertThrows(IllegalArgumentException.class, () -> intraCurrencyBankTransfer.setDestination(currency1_bank2));

        assertDoesNotThrow(() -> intraCurrencyBankTransfer.setDestination(currency2_bank2));
        assertDoesNotThrow(() -> intraCurrencyBankTransfer.setDestination(currency2_bank1));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(IntraCurrencyBankTransfer.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(IntraCurrencyBankTransfer.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Run the validator
     */
    @Test
    void validateIntraCurrencyBankTransfer() {
        assertDoesNotThrow(TrackingDatabase_Integrity::validateIntraCurrencyBankTransfer);
    }
}
