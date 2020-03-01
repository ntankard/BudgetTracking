package com.ntankard.ClassExtension;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.*;

public class ParameterMapTest {

    /**
     * Test that ParameterMap is on all classes and used correctly
     */
    @SuppressWarnings("rawtypes")
    @Test
    void parameterMapUse() {
        for (Class<? extends DataObject> aClass : getAllClasses()) {

            // Get the constructor
            Constructor[] constructors = aClass.getConstructors();
            assertEquals(1, constructors.length, "Multiple constructors detected." + " Class:" + aClass.getSimpleName());

            // Get the ParameterMap
            ParameterMap parameterMap = (ParameterMap) constructors[0].getAnnotation(ParameterMap.class);
            assertNotNull(parameterMap, "Constructor dose not have ParameterMap defined. " + " Class:" + aClass.getSimpleName());

            if (!parameterMap.shouldSave()) {
                continue;
            }

            // Check the count
            assertEquals(constructors[0].getParameterCount(), parameterMap.parameterGetters().length, "Listed parameterGetters dose not match the constructors parameters" + " Class:" + aClass.getSimpleName());

            // Check that the listed methods exist
            for (int i = 0; i < constructors[0].getParameterCount(); i++) {
                String name = parameterMap.parameterGetters()[i];
                Class type = constructors[0].getParameterTypes()[i];

                final Method[] method = new Method[1];
                assertDoesNotThrow(() -> method[0] = aClass.getMethod(name), "Method listed dose not exist." + " Class:" + aClass.getSimpleName() + " Method:" + name);

                if (type.isAssignableFrom(method[0].getReturnType())) {
                    assertEquals(type, method[0].getReturnType(), "Method type dose not match the constructor" + " Class:" + aClass.getSimpleName() + " Method:" + name);
                }
            }
        }
    }
}
