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
class HasDefaultTest {

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
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that all default values are set and correct
     */
    @Test
    void validateDefault() {
        for (DataObject dataObject : database.get(DataObject.class)) {
            boolean found = false;
            for (DataField<?> field : dataObject.getFields()) {
                if (field.getDataFieldSchema().isDefaultFlag()) {
                    found = true;
                    assertEquals(field.getDataFieldSchema().getIdentifierName(), dataObject.getSourceSchema().getDefaultFieldKey());
                    if (dataObject.<Boolean>get(field.getDataFieldSchema().getIdentifierName())) {
                        assertEquals(dataObject, dataObject.getTrackingDatabase().getDefault(dataObject.getClass()));
                    } else {
                        assertNotEquals(dataObject, dataObject.getTrackingDatabase().getDefault(dataObject.getClass()));
                    }
                }
            }
            if (!found) {
                assertNull(dataObject.getSourceSchema().getDefaultFieldKey());
            }
        }
    }
}
