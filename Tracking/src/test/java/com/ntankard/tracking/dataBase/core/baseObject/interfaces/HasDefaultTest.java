package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.HasDefault;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.CONCURRENT)
class HasDefaultTest {

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
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that all default values are set and correct
     */
    @Test
    void validateDefault() {
        for (Class<? extends DataObject> aClass : trackingDatabase.getDataObjectTypes()) {
            if (HasDefault.class.isAssignableFrom(aClass)) {
                assertNotNull(trackingDatabase.getDefault(aClass), "Core Database error. An object with a default value has no default set");
                assertTrue(((HasDefault) trackingDatabase.getDefault(aClass)).isDefault(), "Core Database error. A non default object has been set as the default");
            }
        }
    }
}