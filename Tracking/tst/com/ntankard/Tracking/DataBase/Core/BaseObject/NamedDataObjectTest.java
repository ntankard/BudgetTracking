package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NamedDataObjectTest {

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
        assertDoesNotThrow(() -> new NamedDataObject_Inst(0, "Test"));
        assertThrows(IllegalArgumentException.class, () -> new NamedDataObject_Inst(0, null));
    }

    /**
     * Test setName parameters
     */
    @Test
    void setName() {
        NamedDataObject_Inst namedDataObject_inst = new NamedDataObject_Inst(0, "Test");
        assertDoesNotThrow(() -> namedDataObject_inst.setName(""));
        assertThrows(IllegalArgumentException.class, () -> namedDataObject_inst.setName(null));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test getName values
     */
    @Test
    void getName() {
        for (NamedDataObject namedDataObject : TrackingDatabase.get().get(NamedDataObject.class)) {
            assertNotNull(namedDataObject.getName());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Special Objects #################################################
    //------------------------------------------------------------------------------------------------------------------

    private static class NamedDataObject_Inst extends NamedDataObject {

        NamedDataObject_Inst(Integer id, String name) {
            super(id, name);
        }

        @Override
        public List<DataObject> getParents() {
            return null;
        }
    }
}