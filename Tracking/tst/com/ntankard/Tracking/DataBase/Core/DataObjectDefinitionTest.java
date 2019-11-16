package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.ClassExtension.Util;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataObjectDefinitionTest {

    private static List<Class<? extends SpecialValues>> specialValuesClasses = new ArrayList<>();
    private static List<Class<? extends HasDefault>> hasDefaultClasses = new ArrayList<>();
    private static List<Class<? extends DataObject>> solidClasses = new ArrayList<>();
    private static List<Class<? extends DataObject>> abstractClasses = new ArrayList<>();

    @BeforeAll
    static void setUp() throws IOException, ClassNotFoundException {
        Class[] classes = Util.getClasses("com.ntankard.Tracking.DataBase.Core");

        for (Class aClass : classes) {
            if (DataObject.class.isAssignableFrom(aClass)) {
                if (Modifier.isAbstract(aClass.getModifiers())) {
                    abstractClasses.add(aClass);
                } else {
                    solidClasses.add(aClass);
                }
                if (SpecialValues.class.isAssignableFrom(aClass)) {
                    specialValuesClasses.add(aClass);
                }
                if (HasDefault.class.isAssignableFrom(aClass)) {
                    hasDefaultClasses.add(aClass);
                }
            }
        }
    }

    @Test
    void checkParameterMap() {
        for (Class<? extends DataObject> aClass : solidClasses) {

            Constructor[] constructors = aClass.getConstructors();
            assertEquals(1, constructors.length);

            ParameterMap parameterMap = (ParameterMap) constructors[0].getAnnotation(ParameterMap.class);
            assertNotNull(parameterMap);

            if (parameterMap.shouldSave()) {
                assertEquals(constructors[0].getParameterCount(), parameterMap.parameterGetters().length);
            }

            for (int i = 0; i < constructors[0].getParameterCount(); i++) {
                String name = parameterMap.parameterGetters()[i];
                Class type = constructors[0].getParameterTypes()[i];

                final Method[] method = new Method[1];
                assertDoesNotThrow(() -> {
                    method[0] = aClass.getMethod(name);
                });

                assertEquals(type, method[0].getReturnType());
            }
        }
    }

    @Test
    void checkDisplayProperties() {
        for (Class<? extends DataObject> aClass : solidClasses) {
            MemberClass mClass = new MemberClass(aClass);
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE);

            List<Integer> baseOrder = new ArrayList<>();
            List<Integer> secondOrder = new ArrayList<>();
            for (Member member : members) {
                DisplayProperties properties = member.getGetter().getAnnotation(DisplayProperties.class);
                assertNotNull(properties);

                int order = properties.order();
                List<Integer> toAdd = baseOrder;
                if (order >= 20) {
                    toAdd = secondOrder;
                }

                assertFalse(toAdd.contains(order));
                toAdd.add(order);
            }

            baseOrder.sort(Integer::compareTo);
            secondOrder.sort(Integer::compareTo);

            int last = 0;
            for (Integer integer : baseOrder) {
                assertEquals(last++ + 1, integer);
            }

            last = 19;
            for (Integer integer : secondOrder) {
                assertEquals(last++ + 1, integer);
            }
        }
    }
}
