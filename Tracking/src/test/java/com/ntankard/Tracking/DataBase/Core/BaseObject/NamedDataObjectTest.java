package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject.NamedDataObject_Name;
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
        assertDoesNotThrow(() -> NamedDataObject_Inst.make(0, "Test"));
        assertThrows(IllegalArgumentException.class, () -> NamedDataObject_Inst.make(0, null));
    }

    /**
     * Test setName parameters
     */
    @Test
    void setName() {
        NamedDataObject_Inst namedDataObject_inst = NamedDataObject_Inst.make(0, "Test");
        namedDataObject_inst.add();
        assertDoesNotThrow(() -> namedDataObject_inst.set(NamedDataObject_Name, ""));
        assertThrows(IllegalArgumentException.class, () -> namedDataObject_inst.set(NamedDataObject_Name, null));
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

    public static class NamedDataObject_Inst extends NamedDataObject {

        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = NamedDataObject.getFieldContainer();
            return dataObjectSchema.finaliseContainer(NamedDataObject_Inst.class);
        }

        public static NamedDataObject_Inst make(Integer id, String name) {
            return assembleDataObject(NamedDataObject_Inst.getFieldContainer(), new NamedDataObject_Inst()
                    , DataObject_Id, id
                    , NamedDataObject_Name, name
            );
        }
    }
}