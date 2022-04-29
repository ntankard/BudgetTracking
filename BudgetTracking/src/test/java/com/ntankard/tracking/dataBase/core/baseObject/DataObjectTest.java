package com.ntankard.budgetTracking.dataBase.core.baseObject;

import com.ntankard.budgetTracking.Main;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.validator.FieldValidator;
import com.ntankard.javaObjectDatabase.dataField.validator.shared.Multi_FieldValidator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
import com.ntankard.testUtil.ClassInspectionUtil;
import com.ntankard.testUtil.DataAccessUntil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Util.getVerbosityDataFields;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class DataObjectTest {

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

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

//    /**
//     * Test constructor parameters
//     */
//    @Test
//    void constructor() {
//        assertDoesNotThrow(() -> DataObject_Inst(0));
//        assertThrows(IllegalArgumentException.class, () -> DataObject_Inst(null));
//    }

    /**
     * Test the setters validate there inputs properly
     */
    @SuppressWarnings("unchecked")
    @Test
    @Execution(ExecutionMode.SAME_THREAD)
    void set() {
        assertNotEquals(0, database.getAll().size());

        for (Class<? extends DataObject> testClass : database.getDataObjectTypes()) {
            if (!Modifier.isAbstract(testClass.getModifiers())) {
                for (DataObject dataObject : database.get(testClass)) {
                    Class<? extends DataObject> aClass = dataObject.getClass();
                    List<DataField_Schema<?>> members = getVerbosityDataFields(database.getSchema().getClassSchema(aClass), Integer.MAX_VALUE);

                    // Find the setters
                    for (DataField_Schema member : members) {
                        if (member.getCanEdit() && ((Display_Properties) member.<Display_Properties>getProperty(Display_Properties.class)).getDisplaySet() && DataObject.class.isAssignableFrom(member.getType())) {
                            List<FieldValidator> validators = member.getValidators();
                            boolean breakOut = false;
                            for (FieldValidator validator : validators) {
                                if (Multi_FieldValidator.class.isAssignableFrom(validator.getClass())) {
                                    breakOut = true;
                                    break;
                                }
                            }
                            if (breakOut) {
                                continue;
                            }

                            // Get the data
                            AtomicReference<List<DataObject>> expectedOptions = new AtomicReference<>();
                            assertDoesNotThrow(() -> expectedOptions.set((List) member.getSource().invoke(dataObject, member.getType(), member.getDisplayName())));
                            List<DataObject> fullOptions = new ArrayList(database.get(member.getType()));

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
                                assertThrows(NonCorruptingException.class, () -> dataObject.set(member.getIdentifierName(), null));
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
    @Execution(ExecutionMode.SAME_THREAD)
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
    @Execution(ExecutionMode.SAME_THREAD)
    void checkNonPrimitive() {
        for (Class<? extends DataObject> toTest : database.getSchema().getSolidClasses()) {
            List<DataField_Schema<?>> members = getVerbosityDataFields(database.getSchema().getClassSchema(toTest), Integer.MAX_VALUE);

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
    @Execution(ExecutionMode.SAME_THREAD)
    void getId() {
        for (DataObject dataObject : database.get(DataObject.class)) {
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
    @Execution(ExecutionMode.SAME_THREAD)
    void validateId() {
        for (DataObject dataObject : database.getAll()) {
            for (DataObject toTest : database.getAll()) {
                if (!dataObject.equals(toTest)) {
                    assertNotEquals(dataObject.getId(), toTest.getId(), "Core Database error. Duplicate ID found");
                }
            }
        }
    }

    /**
     * Confirm that all children that the object knows about are present and connected to the parent
     */
    @Test
    @Execution(ExecutionMode.SAME_THREAD)
    void validateChild() {
        for (DataObject dataObject : database.getAll()) {
            for (DataObject child : dataObject.getChildren()) {
                assertNotNull(child, "Core Database error. Null child detected");
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Special Objects #################################################
    //------------------------------------------------------------------------------------------------------------------

    private static class DataObject_Inst extends DataObject {

        public static DataObject_Schema getDataObjectSchema() {
            DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();
            return dataObjectSchema.finaliseContainer(DataObject_Inst.class);
        }

        /**
         * Constructor
         */
        public DataObject_Inst(Database database) {
            super(database);
        }
    }
}
