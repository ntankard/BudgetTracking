package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.testUtil.ClassInspectionUtil;
import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.SpecialValues;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class SpecialValuesTest {

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
     * Check that all special values classes present all there keys
     */
    @Test
    void getKeys() {
        assertNotEquals(0, ClassInspectionUtil.getSpecialValueClasses(Main.databasePath).size());
        for (Class<? extends DataObject> toTest : ClassInspectionUtil.getSpecialValueClasses(Main.databasePath)) {

            // Get the keys
            assertTrue(trackingDatabase.get(toTest).size() != 0, "No class of the test type exists in the current database." + " Class:" + toTest.getSimpleName());
            SpecialValues specialValues = (SpecialValues) trackingDatabase.get(toTest).get(0);
            List<Integer> expected = specialValues.toChangeGetKeys();

            Field[] allFields = toTest.getDeclaredFields();
            for (Field field : allFields) {

                // Find the keys
                Pattern p = Pattern.compile("[A-Z]*");
                if (p.matcher(field.getName()).matches()) {

                    // Find the value of the key
                    AtomicReference<Integer> value = new AtomicReference<>();
                    assertDoesNotThrow(() -> value.set(Integer.parseInt(field.get(null).toString())), "Field value is not integer. Possibly other constant." + " Class:" + toTest.getSimpleName() + " Field:" + field.getName());

                    // Check that the key was presented as available
                    assertTrue(expected.contains(value.get()), "The key was not presented by the class. " + " Class:" + toTest.getSimpleName() + " Field:" + field.getName());
                    expected.remove(value.get());
                }
            }

            // Check that all keys where publicly presented
            assertEquals(0, expected.size(), "Some keys keys do not have public members to expose them." + " Class:" + toTest.getSimpleName());
        }
    }

    /**
     * Check that we can get a value for valid keys, and not values for other keys
     */
    @Test
    void isValue() {
        assertNotEquals(0, ClassInspectionUtil.getSpecialValueClasses(Main.databasePath).size());
        for (Class<? extends DataObject> toTest : ClassInspectionUtil.getSpecialValueClasses(Main.databasePath)) {

            // Get the valid keys
            assertTrue(trackingDatabase.get(toTest).size() != 0, "No class of the test type exists in the current database." + " Class:" + toTest.getSimpleName());
            SpecialValues specialValues = (SpecialValues) trackingDatabase.get(toTest).get(0);
            List<Integer> validKeys = specialValues.toChangeGetKeys();

            // Get a range on invalid keys
            List<Integer> invalidKeys = new ArrayList<>();
            for (int i = -500; i < 500; i++) {
                invalidKeys.add(i);
            }
            invalidKeys.removeAll(validKeys);

            for (Integer valid : validKeys) {
                assertDoesNotThrow(() -> specialValues.isValue(valid), "Class threw an exception when given a valid key" + " Class:" + toTest.getSimpleName() + " Key:" + valid.toString());
            }

            for (Integer invalid : invalidKeys) {
                assertThrows(IllegalArgumentException.class, () -> specialValues.isValue(invalid), "Class did not throw an exception when given a invalid key" + " Class:" + toTest.getSimpleName() + " Key:" + invalid.toString());
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that all special values are present in the database, and are the correct ones
     */
    @Test
    void validateSpecial() {
        for (Class<? extends DataObject> aClass : trackingDatabase.getDataObjectTypes()) {
            if (trackingDatabase.get(aClass).size() != 0) {
                if (SpecialValues.class.isAssignableFrom(aClass)) {
                    for (int key : ((SpecialValues) trackingDatabase.get(aClass).get(0)).toChangeGetKeys()) {
                        assertNotNull(trackingDatabase.getSpecialValue(aClass, key), "Core Database error. An object with a special value has no special value set");
                    }
                }
            }
        }
    }
}
