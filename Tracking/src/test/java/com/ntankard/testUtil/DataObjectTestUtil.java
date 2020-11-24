package com.ntankard.testUtil;

import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Util.getVerbosityDataFields;
import static org.junit.jupiter.api.Assertions.*;

public class DataObjectTestUtil {

    /**
     * Check that the exposed parents match all getter methods that return a DataObject. Not called in this test as this is not true for all objects. Should be called by all children
     */
    public static void testStandardParents(Database database, Class<? extends DataObject> aClass) {
        testStandardParents(database, aClass, new ArrayList<>());
    }

    /**
     * Check that the exposed parents match all getter methods that return a DataObject. Not called in this test as this is not true for all objects. Should be called by all children
     */
    public static void testStandardParents(Database database, Class<? extends DataObject> aClass, List<String> exclude) {
        assertNotEquals(0, database.get(aClass).size());
        for (DataObject toTest : database.get(aClass)) {
            List<DataField_Schema<?>> members = getVerbosityDataFields(database.getSchema().getClassSchema(toTest.getClass()), Integer.MAX_VALUE);

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
    public static void checkDataObjectNotNull(Database database, Class<? extends DataObject> aClass) {
        checkDataObjectNotNull(database, aClass, new ArrayList<>());
    }

    /**
     * Check that data objects are not null
     */
    public static void checkDataObjectNotNull(Database database, Class<? extends DataObject> aClass, List<String> exclude) {
        assertNotEquals(0, database.getAll().size());
        for (DataObject toTest : database.get(aClass)) {
            List<DataField_Schema<?>> members = getVerbosityDataFields(database.getSchema().getClassSchema(toTest.getClass()), Integer.MAX_VALUE);

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
