package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class OrderedTest {

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

    /**
     * Check for a clear order in all cases
     */
    @Test
    void testStandardOrder() {
        for (Class<? extends DataObject> aClass : database.getSchema().getSolidClasses()) {
            if (Ordered.class.isAssignableFrom(aClass)) {
                List<Integer> order = new ArrayList<>();
                assertNotEquals(0, database.get(aClass).size(), "Cant test, no objects of this type exist");
                for (DataObject object : database.get(aClass)) {
                    Ordered ordered = (Ordered) object;
                    assertFalse(order.contains(ordered.getOrder()), "Duplicate order value. " + " Class " + aClass.getSimpleName());
                    order.add(ordered.getOrder());
                }
            }
        }
    }
}
