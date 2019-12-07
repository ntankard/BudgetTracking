package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Integrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasDefaultTest {

    /**
     * Load the data
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Run the validator
     */
    @Test
    void validateDefault() {
        assertDoesNotThrow(TrackingDatabase_Integrity::validateDefault);
    }
}
