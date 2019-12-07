package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(Category.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(Category.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the order is correct
     */
    @Test
    void getOrder() {
        DataObjectTestUtil.testStandardOrder(Category.class);
    }
}
