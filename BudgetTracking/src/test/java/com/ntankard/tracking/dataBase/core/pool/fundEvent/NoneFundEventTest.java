package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.testUtil.DataObjectTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class NoneFundEventTest {

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

    @Test
    void constructor() {
        assertNotEquals(0, database.get(SolidCategory.class).size());

        SolidCategory solidCategory = database.get(SolidCategory.class).get(0);

        assertThrows(NullPointerException.class, () -> new NoneFundEvent("", null, false));
        assertDoesNotThrow(() -> new NoneFundEvent("", solidCategory, false));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(database, NoneFundEvent.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(database, NoneFundEvent.class);
    }
}
