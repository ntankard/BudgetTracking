package com.ntankard.ClassExtension;

import com.ntankard.TestUtil.ClassInspectionUtil;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SetterPropertiesTest {

    /**
     * Check that all setters that set dataObject have source setup properly
     */
    @Test
    void localSourceMethod() {
        assertNotEquals(0, ClassInspectionUtil.getSolidClasses().size());
        for (Class<? extends DataObject> aClass : ClassInspectionUtil.getAllClasses()) {
            MemberClass mClass = new MemberClass(aClass);
            List<Member> members = mClass.getVerbosityMembers(Integer.MAX_VALUE, false);

            for (Member member : members) {
                if (member.getSetter() != null && DataObject.class.isAssignableFrom(member.getType())) {
                    SetterProperties properties = member.getSetter().getAnnotation(SetterProperties.class);
                    assertNotNull(properties, "Setter is missing a source deceleration. " + "Class:" + aClass.getSimpleName() + " Member:" + member.getSetter().getName());
                    assertDoesNotThrow(() -> aClass.getMethod(properties.localSourceMethod(), Class.class, String.class), "Listed method missing." + " Class:" + aClass.getSimpleName() + " Method:" + properties.localSourceMethod());
                }
            }
        }
    }
}
