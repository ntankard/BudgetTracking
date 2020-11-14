package com.ntankard.testUtil;

import com.ntankard.javaObjectDatabase.util.SourceCodeInspector;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.HasDefault;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.SpecialValues;
import com.ntankard.javaObjectDatabase.database.subContainers.DataObjectClassTree;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClassInspectionUtil {

    /**
     * Get all instantiatable classes in the PATH that extend DataObject
     *
     * @return All instantiatable classes in the PATH that extend DataObject
     */
    @SuppressWarnings("unchecked")
    public static List<Class<? extends DataObject>> getSolidClasses(String path) {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = SourceCodeInspector.getClasses(path));

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
    public static List<Class<? extends DataObject>> getAbstractClasses(String path) {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = SourceCodeInspector.getClasses(path));

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
     * Get all classes in the PATH that extend DataObject
     *
     * @return All classes in the PATH that extend DataObject
     */
    @SuppressWarnings("rawtypes")
    public static List<Class<DataObject>> getAllClasses(String path) {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = SourceCodeInspector.getClasses(path));

        // Filter to HasDefault
        List<Class<DataObject>> toReturn = new ArrayList<>();
        for (Class aClass : classes[0]) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                toReturn.add(aClass);
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
    public static List<Class<? extends DataObject>> getHasDefaultClasses(String path) {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = SourceCodeInspector.getClasses(path));

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
    public static List<Class<? extends DataObject>> getSpecialValueClasses(String path) {
        // Get all classes
        final Class[][] classes = {new Class[0]};
        assertDoesNotThrow(() -> classes[0] = SourceCodeInspector.getClasses(path));

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

    /**
     * Get all the classes as a tree of there inheritance
     *
     * @return All the classes as a tree of there inheritance
     */
    public static DataObjectClassTree getDataObjectClassTree(String path) {
        DataObjectClassTree dataObjectClassTree = new DataObjectClassTree();
        for (Class<? extends DataObject> dataObjectClass : getAbstractClasses(path)) {
            dataObjectClassTree.add(dataObjectClass);
        }
        for (Class<? extends DataObject> dataObjectClass : getSolidClasses(path)) {
            dataObjectClassTree.add(dataObjectClass);
        }
        return dataObjectClassTree;
    }
}
