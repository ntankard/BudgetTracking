package com.ntankard.tracking.dataBase.core;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class CurrencyTest {

    /**
     * The database instance to use
     */
    private static TrackingDatabase trackingDatabase;

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        trackingDatabase = DataAccessUntil.getDataBase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test getNumberFormat values
     */
    @Test
    void getNumberFormat() {
        for (Currency currency : trackingDatabase.get(Currency.class)) {
            assertNotNull(currency.getNumberFormat());
        }
    }

    /**
     * Test isDefault values
     */
    @Test
    void isDefault() {
        for (Currency currency : trackingDatabase.get(Currency.class)) {
            assertNotNull(currency.isDefault());
        }
    }

    /**
     * Test getToPrimary values
     */
    @Test
    void getToPrimary() {
        for (Currency currency : trackingDatabase.get(Currency.class)) {
            assertNotNull(currency.getToPrimary());
        }
    }

    /**
     * Test getLanguage values
     */
    @Test
    void getLanguage() {
        for (Currency currency : trackingDatabase.get(Currency.class)) {
            assertNotNull(currency.getLanguage());
        }
    }

    /**
     * Test getCountry values
     */
    @Test
    void getCountry() {
        for (Currency currency : trackingDatabase.get(Currency.class)) {
            assertNotNull(currency.getCountry());
        }
    }

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(trackingDatabase, Currency.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(trackingDatabase, Currency.class);
    }
}
