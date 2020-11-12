package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.testUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;

public class OrderedTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase();
    }

    /**
     * Check for a clear order in all cases
     */
    @Test
    void testStandardOrder() {
        for (Class<DataObject> aClass : getAllClasses()) {
            if (Ordered.class.isAssignableFrom(aClass)) {
                List<Integer> order = new ArrayList<>();
                assertNotEquals(0, TrackingDatabase.get().get(aClass).size(), "Cant test, no objects of this type exist");
                for (DataObject object : TrackingDatabase.get().get(aClass)) {
                    Ordered ordered = (Ordered) object;
                    assertFalse(order.contains(ordered.getOrder()), "Duplicate order value. " + " Class " + aClass.getSimpleName());
                    order.add(ordered.getOrder());
                }
            }
        }
    }
}
