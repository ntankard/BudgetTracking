package com.ntankard.tracking.dataBase.core.baseObject;

import com.ntankard.tracking.Main;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.testUtil.ClassInspectionUtil;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class DataObjectTest {

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
        assertNotEquals(0, trackingDatabase.getAll().size());

        for (DataObject dataObject1 : trackingDatabase.getAll()) {
            dataObject1.validateParents();
            dataObject1.validateChildren();
        }

        for (Class<? extends DataObject> testClass : trackingDatabase.getDataObjectTypes()) {
            if (!Modifier.isAbstract(testClass.getModifiers())) {
                for (DataObject dataObject : trackingDatabase.get(testClass)) {
                    Class<? extends DataObject> aClass = dataObject.getClass();
                    List<DataField_Schema<?>> members = trackingDatabase.getSchema().getClassSchema(aClass).getVerbosityDataFields(Integer.MAX_VALUE);

                    // Find the setters
                    for (DataField_Schema member : members) {
                        if (member.getCanEdit() && member.getDisplayProperties().getDisplaySet() && DataObject.class.isAssignableFrom(member.getType())) {
                            // Get the data
                            AtomicReference<List<DataObject>> expectedOptions = new AtomicReference<>();
                            assertDoesNotThrow(() -> expectedOptions.set((List) member.getSource().invoke(dataObject, member.getType(), member.getDisplayName())));
                            List<DataObject> fullOptions = new ArrayList(trackingDatabase.get(member.getType()));

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
                            if (!(member).isCanBeNull()) {
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
        assertNotEquals(0, ClassInspectionUtil.getSolidClasses(Main.databasePath).size());
        for (Class<? extends DataObject> toTest : ClassInspectionUtil.getSolidClasses(Main.databasePath)) {
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
        for (Class<? extends DataObject> toTest : trackingDatabase.getSchema().getSolidClasses()) {
            List<DataField_Schema<?>> members = trackingDatabase.getSchema().getClassSchema(toTest).getVerbosityDataFields(Integer.MAX_VALUE);

            // Find the setters
            for (DataField_Schema member : members) {
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
        for (DataObject dataObject : trackingDatabase.get(DataObject.class)) {
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
        for (DataObject dataObject : trackingDatabase.getAll()) {
            for (DataObject toTest : trackingDatabase.getAll()) {
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

        for (DataObject dataObject : trackingDatabase.getAll()) {
            dataObject.validateParents();
            for (DataObject parent : dataObject.getParents()) {
                if (!(parent instanceof Currency)) {
                    assertNotNull(parent, "Core Database error. Null parent detected");
                    assertTrue(parent.getChildren().contains(dataObject), "Core Database error. Parent has not been notified");
                }
            }
        }
    }

    /**
     * Confirm that all children that the object knows about are present and connected to the parent
     */
    @Test
    void validateChild() {
        for (DataObject dataObject : trackingDatabase.getAll()) {
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

        public static DataObject_Schema getFieldContainer() {
            DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();
            return dataObjectSchema.finaliseContainer(DataObject_Inst.class);
        }

        public static DataObject_Inst make(Integer id) {
            return assembleDataObject(trackingDatabase, DataObject_Inst.getFieldContainer(), new DataObject_Inst()
                    , DataObject_Id, id
            );
        }
    }
}
