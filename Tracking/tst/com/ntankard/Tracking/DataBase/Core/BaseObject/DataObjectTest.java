package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.TestUtil.ClassInspectionUtil;
import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class DataObjectTest {

    /**
     * Load the data
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); // TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test constructor parameters
     */
    @Test
    void constructor() {
        assertDoesNotThrow(() -> new DataObject_Inst(0, null));
        assertThrows(IllegalArgumentException.class, () -> new DataObject_Inst(null, null));
    }

    /**
     * Test the notifying the parent results in the correct link
     */
    @Test
    void notifyParentLink() {
        DataObject_Inst parent = new DataObject_Inst(0, null);
        assertEquals(0, parent.getChildren().size());

        DataObject_Inst child1 = new DataObject_Inst(1, parent);
        DataObject_Inst child2 = new DataObject_Inst(2, parent);
        DataObject_Inst child3 = new DataObject_Inst(3, parent);
        assertEquals(0, parent.getChildren().size());

        child1.notifyParentLink();
        assertEquals(1, parent.getChildren().size());
        assertTrue(parent.getChildren().contains(child1));
        assertFalse(parent.getChildren().contains(child2));
        assertFalse(parent.getChildren().contains(child3));
        assertThrows(Exception.class, child1::notifyParentLink, "Can add the same child twice");

        child2.notifyParentLink();
        assertEquals(2, parent.getChildren().size());
        assertTrue(parent.getChildren().contains(child1));
        assertTrue(parent.getChildren().contains(child2));
        assertFalse(parent.getChildren().contains(child3));
        assertThrows(Exception.class, child2::notifyParentLink, "Can add the same child twice");

        child3.notifyParentLink();
        assertEquals(3, parent.getChildren().size());
        assertTrue(parent.getChildren().contains(child1));
        assertTrue(parent.getChildren().contains(child2));
        assertTrue(parent.getChildren().contains(child3));
        assertThrows(Exception.class, child3::notifyParentLink, "Can add the same child twice");
    }

    /**
     * Test the notifying the parent results in the correct un link
     */
    @Test
    void notifyParentUnLink() {
        DataObject_Inst parent = new DataObject_Inst(0, null);

        DataObject_Inst child1 = new DataObject_Inst(1, parent);
        DataObject_Inst child2 = new DataObject_Inst(2, parent);
        DataObject_Inst child3 = new DataObject_Inst(3, parent);

        child1.notifyParentLink();
        child2.notifyParentLink();
        child3.notifyParentLink();

        child1.notifyParentUnLink();
        assertThrows(Exception.class, child1::notifyParentUnLink, "Can remove the same child twice");
        assertEquals(2, parent.getChildren().size());
        assertFalse(parent.getChildren().contains(child1));
        assertTrue(parent.getChildren().contains(child2));
        assertTrue(parent.getChildren().contains(child3));

        child2.notifyParentUnLink();
        assertThrows(Exception.class, child2::notifyParentUnLink, "Can remove the same child twice");
        assertEquals(1, parent.getChildren().size());
        assertFalse(parent.getChildren().contains(child1));
        assertFalse(parent.getChildren().contains(child2));
        assertTrue(parent.getChildren().contains(child3));

        child3.notifyParentUnLink();
        assertThrows(Exception.class, child3::notifyParentUnLink, "Can remove the same child twice");
        assertEquals(0, parent.getChildren().size());
        assertFalse(parent.getChildren().contains(child1));
        assertFalse(parent.getChildren().contains(child2));
        assertFalse(parent.getChildren().contains(child3));
    }

    /**
     * Test the setters validate there inputs properly
     */
    @SuppressWarnings("unchecked")
    @Test
    void set() {
        assertNotEquals(0, TrackingDatabase.get().getAll().size());
        for (Class<? extends DataObject> testClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (!Modifier.isAbstract(testClass.getModifiers())) {
                for (DataObject dataObject : TrackingDatabase.get().get(testClass)) {
                    Class<? extends DataObject> aClass = dataObject.getClass();
                    MemberClass mClass = new MemberClass(aClass);
                    List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

                    // Find the setters
                    for (Member member : members) {
                        if (member.getSetter() != null && DataObject.class.isAssignableFrom(member.getType())) {
                            SetterProperties properties = member.getSetter().getAnnotation(SetterProperties.class);
                            assertNotNull(properties, "Setter is missing a source deceleration. " + "Class:" + aClass.getSimpleName() + " Member:" + member.getSetter().getName());

                            if (properties.displaySet()) {
                                // Get the data
                                AtomicReference<List<DataObject>> expectedOptions = new AtomicReference<>();
                                assertDoesNotThrow(() -> expectedOptions.set((List) member.getSource().invoke(dataObject, member.getType(), member.getName())));
                                List<DataObject> fullOptions = new ArrayList(TrackingDatabase.get().get(member.getType()));

                                // Check valid values
                                for (DataObject valid : expectedOptions.get()) {
                                    assertDoesNotThrow(() -> member.getSetter().invoke(dataObject, valid), "A valid value was rejected from a method" + "DataObject:" + dataObject.toString() + " Class:" + aClass.getSimpleName() + " Setter:" + member.getSetter().getName());
                                    fullOptions.remove(valid);
                                }

                                // Check invalid values
                                for (DataObject invalid : fullOptions) {
                                    assertThrows(Exception.class, () -> member.getSetter().invoke(dataObject, invalid), "A invalid value was allowed to be set from a method." + " DataObject:" + dataObject.toString() + " Class:" + aClass.getSimpleName() + " Setter:" + member.getSetter().getName() + " ValidValue:" + invalid.toString());
                                }

                                // Check null
                                assertThrows(IllegalArgumentException.class, () -> member.getSetter().invoke(dataObject, null), "A null value was allowed to be set from a method." + " DataObject:" + dataObject.toString() + " Class:" + aClass.getSimpleName() + " Setter:" + member.getSetter().getName() + " ValidValue:");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Test that no object inherits from a non abstract object
     */
    @SuppressWarnings("unchecked")
    @Test
    void checkAbstractInheritance() {
        assertNotEquals(0, ClassInspectionUtil.getSolidClasses().size());
        for (Class<? extends DataObject> toTest : ClassInspectionUtil.getSolidClasses()) {
            Class<? extends DataObject> aClass = toTest;

            assertFalse(Modifier.isAbstract(aClass.getModifiers()));
            aClass = (Class<? extends DataObject>) aClass.getSuperclass();
            do {
                assertTrue(Modifier.isAbstract(aClass.getModifiers()), "Class " + toTest.getSimpleName() + " inherit from " + aClass.getSimpleName() + " which is no abstract");

                // Jump up the inheritance tree
                aClass = (Class<? extends DataObject>) aClass.getSuperclass();
            } while (DataObject.class.isAssignableFrom(aClass));
        }
    }

    /**
     * Check that primitives are not used (i cant remember why this matters)
     */
    @Test
    void checkNonPrimitive() {
        for (Class<? extends DataObject> toTest : ClassInspectionUtil.getAllClasses()) {
            MemberClass mClass = new MemberClass(toTest);
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

            // Find the setters
            for (Member member : members) {
                assertFalse(member.getType().isPrimitive(), "A member is defined primitive" + " Class:" + toTest.getSimpleName() + " Method:" + member.getName());
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Test that no ID is null
     */
    @Test
    void getId() {
        for (DataObject dataObject : TrackingDatabase.get().get(DataObject.class)) {
            assertNotNull(dataObject.getId());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm the ID of all object. Check they are unique
     */
    @Test
    void validateId() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject toTest : TrackingDatabase.get().getAll()) {
                if (!dataObject.equals(toTest)) {
                    assertNotEquals(dataObject.getId(), toTest.getId(), "Core Database error. Duplicate ID found");
                }
            }
        }
    }

    /**
     * Confirm that all parent objects are present and have been linked
     */
    @Test
    void validateParent() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject parent : dataObject.getParents()) {
                assertNotNull(parent, "Core Database error. Null parent detected");
                assertTrue(parent.getChildren().contains(dataObject), "Core Database error. Parent has not been notified");
            }
        }
    }

    /**
     * Confirm that all children that the object knows about are present and connected to the parent
     */
    @Test
    void validateChild() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject child : dataObject.getChildren()) {
                assertNotNull(child, "Core Database error. Null child detected");
                assertTrue(child.getParents().contains(dataObject), "Core Database error. Object registers as a child that dose not list this object as a parent");
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Special Objects #################################################
    //------------------------------------------------------------------------------------------------------------------

    private static class DataObject_Inst extends DataObject {

        private DataObject parent;

        DataObject_Inst(Integer id, DataObject parent) {
            super(id);
            this.parent = parent;
        }

        @Override
        public List<DataObject> getParents() {
            List<DataObject> toReturn = new ArrayList<>();
            toReturn.add(parent);
            return toReturn;
        }
    }
}
