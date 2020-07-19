package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.CoreObject.Field.DataField;
import com.ntankard.CoreObject.FieldContainer;
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
        assertDoesNotThrow(() -> DataObject_Inst.make(0));
        assertThrows(IllegalArgumentException.class, () -> DataObject_Inst.make(null));
    }

    /**
     * Test the setters validate there inputs properly
     */
    @SuppressWarnings("unchecked")
    @Test
    void set() {
        assertNotEquals(0, TrackingDatabase.get().getAll().size());

        for (DataObject dataObject1 : TrackingDatabase.get().getAll()) {
            dataObject1.validateParents();
            dataObject1.validateChildren();
        }

        for (Class<? extends DataObject> testClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (!Modifier.isAbstract(testClass.getModifiers())) {
                for (DataObject dataObject : TrackingDatabase.get().get(testClass)) {
                    Class<? extends DataObject> aClass = dataObject.getClass();
                    List<DataField<?>> members = CoreObject.getFieldContainer(aClass).getVerbosityDataFields(Integer.MAX_VALUE);

                    // Find the setters
                    for (DataField member : members) {
                        if (member.getDataCore().canEdit() && member.getDisplayProperties().getDisplaySet() && DataObject.class.isAssignableFrom(member.getType())) {
                            // Get the data
                            AtomicReference<List<DataObject>> expectedOptions = new AtomicReference<>();
                            assertDoesNotThrow(() -> expectedOptions.set((List) member.getSource().invoke(dataObject, member.getType(), member.getDisplayName())));
                            List<DataObject> fullOptions = new ArrayList(TrackingDatabase.get().get(member.getType()));

                            // Check valid values
                            for (DataObject valid : expectedOptions.get()) {
                                assertDoesNotThrow(() -> dataObject.set(member.getIdentifierName(), valid), "A valid value was rejected from a method" + "DataObject:" + dataObject.toString() + " Class:" + aClass.getSimpleName());
                                fullOptions.remove(valid);
                            }

                            // Check invalid values
                            for (DataObject invalid : fullOptions) {
                                assertThrows(Exception.class, () -> dataObject.set(member.getIdentifierName(), invalid), "A invalid value was allowed to be set from a method." + " DataObject:" + dataObject.toString() + " Class:" + aClass.getSimpleName() + " ValidValue:" + invalid.toString());
                            }

                            // Check null
                            if (!((Tracking_DataField<?>) member).isCanBeNull()) {
                                assertThrows(IllegalArgumentException.class, () -> dataObject.set(member.getIdentifierName(), null));
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
            List<DataField<?>> members = CoreObject.getFieldContainer(toTest).getVerbosityDataFields(Integer.MAX_VALUE);

            // Find the setters
            for (DataField member : members) {
                assertFalse(member.getType().isPrimitive(), "A member is defined primitive" + " Class:" + toTest.getSimpleName() + " Method:" + member.getDisplayName());
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
        // TODO things are missing here. If the field type is not set to data object it wont be registerd as a parent and nothing will catch it, you have to go through each indevidual field and check

        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            dataObject.validateParents();
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

        public static FieldContainer getFieldContainer() {
            FieldContainer fieldContainer = DataObject.getFieldContainer();
            return fieldContainer.finaliseContainer(DataObject_Inst.class);
        }

        public static DataObject_Inst make(Integer id) {
            return assembleDataObject(DataObject_Inst.getFieldContainer(), new DataObject_Inst()
                    , DataObject_Id, id
            );
        }
    }
}
