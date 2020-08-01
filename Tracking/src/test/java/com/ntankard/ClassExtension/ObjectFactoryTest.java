package com.ntankard.ClassExtension;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import org.junit.jupiter.api.Test;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ObjectFactoryTest {

    /**
     * Check that any method that creates other methods overrides add to manage them
     */
    @Test
    void objectFactoryAddTest() {
        for (Class<? extends DataObject> aClass : getAllClasses()) {
            ObjectFactory objectFactory = aClass.getAnnotation(ObjectFactory.class);
            if (objectFactory != null) {
                assertDoesNotThrow(() -> aClass.getDeclaredMethod("add"));
            }
        }
    }

}
