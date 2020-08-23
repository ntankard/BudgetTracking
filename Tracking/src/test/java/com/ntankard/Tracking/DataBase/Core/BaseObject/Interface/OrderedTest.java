package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
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
