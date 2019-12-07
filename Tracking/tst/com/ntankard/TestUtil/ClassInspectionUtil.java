package com.ntankard.TestUtil;

import com.ntankard.ClassExtension.Util;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClassInspectionUtil {

    /**
     * The path to look for classes
     */
    private static String PATH = "com.ntankard.Tracking.DataBase.Core";

    /**
     * Get all instantiatable classes in the PATH that extend DataObject
     *
     * @return All instantiatable classes in the PATH that extend DataObject
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends DataObject>> getSolidClasses() {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = Util.getClasses(PATH));

        // Filter to solid
        List<Class<? extends DataObject>> toReturn = new ArrayList<>();
        for (Class aClass : classes[0]) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                if (!Modifier.isAbstract(aClass.getModifiers())) {
                    toReturn.add(aClass);
                }
            }
        }

        assertNotEquals(0, toReturn.size());
        return toReturn;
    }

    /**
     * Get all abstract classes in the PATH that extend DataObject
     *
     * @return All abstract  classes in the PATH that extend DataObject
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends DataObject>> getAbstractClasses() {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = Util.getClasses(PATH));

        // Filter to abstract
        List<Class<? extends DataObject>> toReturn = new ArrayList<>();
        for (Class aClass : classes[0]) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                if (Modifier.isAbstract(aClass.getModifiers())) {
                    toReturn.add(aClass);
                }
            }
        }

        assertNotEquals(0, toReturn.size());
        return toReturn;
    }

    /**
     * Get all classes that implement HasDefault in the PATH that extend DataObject
     *
     * @return All abstract  classes in the PATH that extend DataObject
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends DataObject>> getHasDefaultClasses() {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = Util.getClasses(PATH));

        // Filter to HasDefault
        List<Class<? extends DataObject>> toReturn = new ArrayList<>();
        for (Class aClass : classes[0]) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                if (HasDefault.class.isAssignableFrom(aClass)) {
                    toReturn.add(aClass);
                }
            }
        }

        assertNotEquals(0, toReturn.size());
        return toReturn;
    }

    /**
     * Get all classes that implement SpecialValues in the PATH that extend DataObject
     *
     * @return All abstract  classes in the PATH that extend DataObject
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends DataObject>> getSpecialValueClasses() {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = Util.getClasses(PATH));

        // Filter to SpecialValues
        List<Class<? extends DataObject>> toReturn = new ArrayList<>();
        for (Class aClass : classes[0]) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                if (SpecialValues.class.isAssignableFrom(aClass)) {
                    toReturn.add(aClass);
                }

            }
        }

        assertNotEquals(0, toReturn.size());
        return toReturn;
    }
}
