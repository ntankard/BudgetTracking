package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

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
        assertDoesNotThrow(() -> new Currency(0, "", false, 0.0, "", ""));
        assertThrows(IllegalArgumentException.class, () -> new Currency(0, "", null, 0.0, "", ""));
        assertThrows(IllegalArgumentException.class, () -> new Currency(0, "", false, null, "", ""));
        assertThrows(IllegalArgumentException.class, () -> new Currency(0, "", false, 0.0, null, ""));
        assertThrows(IllegalArgumentException.class, () -> new Currency(0, "", false, 0.0, "", null));
    }


    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test getNumberFormat values
     */
    @Test
    void getNumberFormat() {
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            assertNotNull(currency.getNumberFormat());
        }
    }

    /**
     * Test isDefault values
     */
    @Test
    void isDefault() {
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            assertNotNull(currency.isDefault());
        }
    }

    /**
     * Test getToPrimary values
     */
    @Test
    void getToPrimary() {
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            assertNotNull(currency.getToPrimary());
        }
    }

    /**
     * Test getLanguage values
     */
    @Test
    void getLanguage() {
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            assertNotNull(currency.getLanguage());
        }
    }

    /**
     * Test getCountry values
     */
    @Test
    void getCountry() {
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            assertNotNull(currency.getCountry());
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
