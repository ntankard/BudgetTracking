package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.testUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class OrderedTest {

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

    /**
     * Check for a clear order in all cases
     */
    @Test
    void testStandardOrder() {
        for (Class<? extends DataObject> aClass : trackingDatabase.getSchema().getSolidClasses()) {
            if (Ordered.class.isAssignableFrom(aClass)) {
                List<Integer> order = new ArrayList<>();
                assertNotEquals(0, trackingDatabase.get(aClass).size(), "Cant test, no objects of this type exist");
                for (DataObject object : trackingDatabase.get(aClass)) {
                    Ordered ordered = (Ordered) object;
                    assertFalse(order.contains(ordered.getOrder()), "Duplicate order value. " + " Class " + aClass.getSimpleName());
                    order.add(ordered.getOrder());
                }
            }
        }
    }
}
