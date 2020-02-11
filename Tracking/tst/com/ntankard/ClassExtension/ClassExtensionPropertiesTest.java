package com.ntankard.ClassExtension;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import org.junit.jupiter.api.Test;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassExtensionPropertiesTest {

    /**
     * Check that all classes include the parents ClassExtensionProperties.includeParent = true
     */
    @Test
    void classExtensionPropertiesUse() {
        for (Class<? extends DataObject> dClass : getAllClasses()) {
            if (!dClass.equals(DataObject.class)) {
                ClassExtensionProperties classExtensionProperties = dClass.getAnnotation(ClassExtensionProperties.class);
                assertNotNull(classExtensionProperties, "Class is missing the Class Extension Properties annotation." + " Class:" + dClass.getSimpleName());
                assertTrue(classExtensionProperties.includeParent(), "All classes must include the parents." + " Class:" + dClass.getSimpleName());
            }
        }
    }
}
