package com.ntankard.tracking.dataBase.core.baseObject;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject.NamedDataObject_Name;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class NamedDataObjectTest {

    /**
     * The database instance to use
     */
    private static Database database;

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        database = DataAccessUntil.getDataBase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test constructor parameters
     */
    @Test
    void constructor() {
        assertDoesNotThrow(() -> NamedDataObject_Inst.make(database,0, "Test"));
        assertThrows(IllegalArgumentException.class, () -> NamedDataObject_Inst.make(database,0, null));
    }

    /**
     * Test setName parameters
     */
    @Test
    void setName() {
        NamedDataObject_Inst namedDataObject_inst = NamedDataObject_Inst.make(database,0, "Test");
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
        for (NamedDataObject namedDataObject : database.get(NamedDataObject.class)) {
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

        public static NamedDataObject_Inst make(Database database, Integer id, String name) {
            return assembleDataObject(database, NamedDataObject_Inst.getFieldContainer(), new NamedDataObject_Inst()
                    , DataObject_Id, id
                    , NamedDataObject_Name, name
            );
        }
    }
}