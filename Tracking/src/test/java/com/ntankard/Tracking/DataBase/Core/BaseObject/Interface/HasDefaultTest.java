package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Confirm that all default values are set and correct
     */
    @Test
    void validateDefault() {
        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (HasDefault.class.isAssignableFrom(aClass)) {
                assertNotNull(TrackingDatabase.get().getDefault(aClass), "Core Database error. An object with a default value has no default set");
                assertTrue(((HasDefault) TrackingDatabase.get().getDefault(aClass)).isDefault(), "Core Database error. A non default object has been set as the default");
            }
        }
    }
}
