package com.ntankard.tracking.dataBase.core.pool;

import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class BankTest {

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
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(trackingDatabase, Bank.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(trackingDatabase, Bank.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------
}