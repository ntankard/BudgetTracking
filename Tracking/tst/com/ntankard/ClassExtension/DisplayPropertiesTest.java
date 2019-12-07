package com.ntankard.ClassExtension;

import com.ntankard.TestUtil.ClassInspectionUtil;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DisplayPropertiesTest {

    /**
     * Check that all object types that support DisplayProperties are orders correctly
     */
    @Test
    void order() {
        assertNotEquals(0, ClassInspectionUtil.getSolidClasses().size());
        for (Class<? extends DataObject> aClass : ClassInspectionUtil.getSolidClasses()) {
            MemberClass mClass = new MemberClass(aClass);
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

            List<Integer> baseOrder = new ArrayList<>();
            List<Integer> secondOrder = new ArrayList<>();
            for (Member member : members) {
                DisplayProperties properties = member.getGetter().getAnnotation(DisplayProperties.class);
                assertNotNull(properties, "Member is missing DisplayProperties." + " Class:" + aClass.getSimpleName() + " Method:" + member.getGetter().getName());

                int order = properties.order();
                List<Integer> toAdd = baseOrder;
                if (order >= 20) {
                    toAdd = secondOrder;
                }

                assertFalse(toAdd.contains(order), "More than 1 method has the same order value." + " Class:" + aClass.getSimpleName() + " Method:" + member.getGetter().getName());
                toAdd.add(order);
            }

            baseOrder.sort(Integer::compareTo);
            secondOrder.sort(Integer::compareTo);

            int last = 0;
            for (Integer integer : baseOrder) {
                assertEquals(last++ + 1, integer, "One of the order values is missing. Missing values is " + last + " Class:" + aClass.getSimpleName());
            }

            last = 20;
            for (Integer integer : secondOrder) {
                assertEquals(last++ + 1, integer, "One of the second order values is missing. Missing values is " + last + " Class:" + aClass.getSimpleName());
            }
        }
    }
}
