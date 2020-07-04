package com.ntankard.ClassExtension;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotationTest {

    /**
     * Check that all known annotations are in the correct order
     */
    @Test
    void classAnnotationOrder() {
        List<Class<? extends Annotation>> expectedAnnotations = new ArrayList<>(Arrays.asList(ParameterMap.class, ObjectFactory.class));

        for (Class<? extends DataObject> dClass : getAllClasses()) {
            int testIndex = -1;
            for (Annotation annotation : dClass.getAnnotations()) {
                while (true) {
                    testIndex++;
                    assertTrue(testIndex < expectedAnnotations.size(), "Class: " + dClass.getName() + " has its constructor parameters out of order");
                    if (annotation.annotationType().equals(expectedAnnotations.get(testIndex))) {
                        break;
                    }
                }
            }
        }
    }
}
