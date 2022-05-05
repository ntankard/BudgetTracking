package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.javaObjectDatabase.dataField.DataField;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.testUtil.DataAccessUntil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class SpecialValuesTest {

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
     * Check that all marked special fields are recognised by the database
     */
    @Test
    void checkSpecial() {
        for (DataObject dataObject : database.get(DataObject.class)) {
            for (DataField<?> field : dataObject.getFields()) {
                if (field.getDataFieldSchema().isSpecialFlag()) {
                    assertTrue(dataObject.getSourceSchema().getSpecialFlagKeys().contains(field.getDataFieldSchema().getIdentifierName()));
                    if (dataObject.<Boolean>get(field.getDataFieldSchema().getIdentifierName())) {
                        assertEquals(dataObject, database.getSpecialValue(dataObject.getClass(), field.getDataFieldSchema().getIdentifierName()));
                    } else {
                        assertNotEquals(dataObject, database.getSpecialValue(dataObject.getClass(), field.getDataFieldSchema().getIdentifierName()));
                    }
                } else {
                    assertFalse(dataObject.getSourceSchema().getSpecialFlagKeys().contains(field.getDataFieldSchema().getIdentifierName()));
                    assertNull(database.getSpecialValue(dataObject.getClass(), field.getDataFieldSchema().getIdentifierName()));
                }
            }
        }
    }
}
