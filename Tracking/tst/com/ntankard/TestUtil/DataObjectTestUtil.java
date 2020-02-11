package com.ntankard.TestUtil;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

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
            MemberClass mClass = new MemberClass(toTest.getClass());
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

            // Find the getters
            List<Member> expectedMember = new ArrayList<>();
            for (Member candidate : members) {
                if (DataObject.class.isAssignableFrom(candidate.getGetter().getReturnType())) {
                    if (!exclude.contains(candidate.getName())) {
                        expectedMember.add(candidate);
                    }
                }
            }

            // Extract all the objects
            List<DataObject> expectedParents = new ArrayList<>();
            for (Member getter : expectedMember) {
                assertDoesNotThrow(() -> {
                    DataObject toAdd = (DataObject) getter.getGetter().invoke(toTest);
                    if (!expectedParents.contains(toAdd)) {
                        expectedParents.add(toAdd);
                    }
                }, "Could not get the object." + " Class:" + aClass.getSimpleName() + " Object:" + toTest.toString() + " Member:" + getter.getName());
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
            MemberClass mClass = new MemberClass(toTest.getClass());
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

            // Find the getters
            List<Member> expectedMember = new ArrayList<>();
            for (Member candidate : members) {
                if (DataObject.class.isAssignableFrom(candidate.getGetter().getReturnType())) {
                    if (!exclude.contains(candidate.getName())) {
                        expectedMember.add(candidate);
                    }
                }
            }

            // Extract all the objects
            for (Member getter : expectedMember) {
                assertDoesNotThrow(() -> {
                    DataObject toAdd = (DataObject) getter.getGetter().invoke(toTest);
                    assertNotNull(toAdd);
                }, "Could not get the object." + " Class:" + toTest.getClass().getSimpleName() + " Object:" + toTest.toString() + " Member:" + getter.getName());
            }
        }
    }
}
