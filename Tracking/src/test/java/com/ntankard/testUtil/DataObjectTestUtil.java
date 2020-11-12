package com.ntankard.testUtil;

import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataObjectTestUtil {

    /**
     * Check that the exposed parents match all getter methods that return a DataObject. Not called in this test as this is not true for all objects. Should be called by all children
     */
    public static void testStandardParents(Class<? extends DataObject> aClass) {
        testStandardParents(aClass, new ArrayList<>());
    }

    /**
     * Check that the exposed parents match all getter methods that return a DataObject. Not called in this test as this is not true for all objects. Should be called by all children
     */
    public static void testStandardParents(Class<? extends DataObject> aClass, List<String> exclude) {
        assertNotEquals(0, TrackingDatabase.get().get(aClass).size());
        for (DataObject toTest : TrackingDatabase.get().get(aClass)) {
            List<DataField_Schema<?>> members = TrackingDatabase_Schema.get().getClassSchema(toTest.getClass()).getVerbosityDataFields(Integer.MAX_VALUE);

            // Find the getters
            List<DataField_Schema<?>> expectedMember = new ArrayList<>();
            for (DataField_Schema<?> candidate : members) {
                if (DataObject.class.isAssignableFrom(candidate.getType())) {
                    if (!exclude.contains(candidate.getDisplayName())) {
                        expectedMember.add(candidate);
                    }
                }
            }

            // Extract all the objects
            List<DataObject> expectedParents = new ArrayList<>();
            for (DataField_Schema<?> getter : expectedMember) {
                assertDoesNotThrow(() -> {
                    DataObject toAdd = toTest.get(getter.getIdentifierName());
                    if (!expectedParents.contains(toAdd)) {
                        expectedParents.add(toAdd);
                    }
                }, "Could not get the object." + " Class:" + aClass.getSimpleName() + " Object:" + toTest.toString() + " Member:" + getter.getDisplayName());
            }

            // Extract the presented objects
            List<DataObject> reportedParents = toTest.getParents();

            assertEquals(expectedParents.size(), reportedParents.size(), "Parents do not match the number of getters." + " Class:" + toTest.getClass().getSimpleName() + " Object:" + toTest.toString());
            for (DataObject dataObject : expectedParents) {
                assertTrue(reportedParents.contains(dataObject), "An object dose not match what was presented." + " Class:" + aClass.getSimpleName() + " TestObject:" + toTest.toString() + " Object:" + dataObject.toString());
            }
        }
    }

    /**
     * Check that data objects are not null
     */
    public static void checkDataObjectNotNull(Class<? extends DataObject> aClass) {
        checkDataObjectNotNull(aClass, new ArrayList<>());
    }

    /**
     * Check that data objects are not null
     */
    public static void checkDataObjectNotNull(Class<? extends DataObject> aClass, List<String> exclude) {
        assertNotEquals(0, TrackingDatabase.get().getAll().size());
        for (DataObject toTest : TrackingDatabase.get().get(aClass)) {
            List<DataField_Schema<?>> members = TrackingDatabase_Schema.get().getClassSchema(toTest.getClass()).getVerbosityDataFields(Integer.MAX_VALUE);

            // Find the getters
            List<DataField_Schema<?>> expectedMember = new ArrayList<>();
            for (DataField_Schema<?> candidate : members) {
                if (DataObject.class.isAssignableFrom(candidate.getType())) {
                    if (!exclude.contains(candidate.getDisplayName())) {
                        expectedMember.add(candidate);
                    }
                }
            }

            // Extract all the objects
            for (DataField_Schema<?> getter : expectedMember) {
                assertDoesNotThrow(() -> {
                    DataObject toAdd = toTest.get(getter.getIdentifierName());
                    assertNotNull(toAdd);
                }, "Could not get the object." + " Class:" + toTest.getClass().getSimpleName() + " Object:" + toTest.toString() + " Member:" + getter.getDisplayName());
            }
        }
    }
}
