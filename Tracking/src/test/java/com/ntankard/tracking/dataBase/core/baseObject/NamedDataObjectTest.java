package com.ntankard.tracking.dataBase.core.baseObject;

import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Collections;
import java.util.List;

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
        List<Class<? extends DataObject>> knownTypes = Collections.singletonList(NamedDataObject_Inst.class);
        database = DataAccessUntil.getDataBase(knownTypes);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test constructor parameters
     */
    @Test
    void constructor() {
        assertDoesNotThrow(() -> new NamedDataObject_Inst(database, "Test"));
        assertThrows(NonCorruptingException.class, () -> new NamedDataObject_Inst(database, null));
    }

    /**
     * Test setName parameters
     */
    @Test
    void setName() {
        NamedDataObject_Inst namedDataObject_inst = new NamedDataObject_Inst(database, "Test");
        namedDataObject_inst.add();
        assertDoesNotThrow(() -> namedDataObject_inst.set(NamedDataObject_Name, ""));
        assertThrows(NonCorruptingException.class, () -> namedDataObject_inst.set(NamedDataObject_Name, null));
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

        public static DataObject_Schema getDataObjectSchema() {
            DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();
            return dataObjectSchema.finaliseContainer(NamedDataObject_Inst.class);
        }

        /**
         * Constructor
         */
        public NamedDataObject_Inst(Database database) {
            super(database);
        }

        /**
         * Constructor
         */
        public NamedDataObject_Inst(Database database, String name) {
            this(database);
            setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                    , NamedDataObject_Name, name
            );
        }
    }
}