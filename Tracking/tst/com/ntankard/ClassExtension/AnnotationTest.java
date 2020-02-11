package com.ntankard.ClassExtension;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ntankard.TestUtil.ClassInspectionUtil.getAllClasses;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotationTest {

    /**
     * Check that all known annotations are in the correct order
     */
    @Test
    void methodAnnotationOrder() {
        List<Class<? extends Annotation>> expectedAnnotations = new ArrayList<>(Arrays.asList(MemberProperties.class, DisplayProperties.class));

        for (Class<? extends DataObject> dClass : getAllClasses()) {
            for (Member member : new MemberClass(dClass).getVerbosityMembers(Integer.MAX_VALUE, false)) {
                Annotation[] all = member.getGetter().getAnnotations();
                assertTrue(all.length < 3);
                if (all.length == 1) {
                    assertTrue(expectedAnnotations.contains(all[0].annotationType()));
                } else if (all.length == 2) {
                    for (int i = 0; i < 2; i++) {
                        assertTrue(expectedAnnotations.contains(all[i].annotationType()), "An unknown annotation has been detected" + " Class:" + dClass.getSimpleName() + " Method:" + member.getGetter().getName());
                        assertEquals(expectedAnnotations.get(i), all[i].annotationType(), "Annotations are out of order" + " Class:" + dClass.getSimpleName() + " Method:" + member.getGetter().getName());
                    }
                }
            }
        }
    }

    /**
     * Check that all known annotations are in the correct order
     */
    @Test
    void classAnnotationOrder() {
        List<Class<? extends Annotation>> expectedAnnotations = new ArrayList<>(Arrays.asList(ClassExtensionProperties.class, ObjectFactory.class));

        for (Class<? extends DataObject> dClass : getAllClasses()) {
            Annotation[] all = dClass.getAnnotations();
            assertTrue(all.length < 3);
            if (all.length == 1) {
                assertTrue(expectedAnnotations.contains(all[0].annotationType()));
            } else if (all.length == 2) {
                for (int i = 0; i < 2; i++) {
                    assertTrue(expectedAnnotations.contains(all[i].annotationType()), "An unknown annotation has been detected" + " Class:" + dClass.getSimpleName());
                    assertEquals(expectedAnnotations.get(i), all[i].annotationType(), "Annotations are out of order" + " Class:" + dClass.getSimpleName());
                }
            }
        }
    }
}
