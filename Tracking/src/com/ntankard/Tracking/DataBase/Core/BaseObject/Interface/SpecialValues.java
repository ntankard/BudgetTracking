package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public interface SpecialValues {

    /**
     * Is this object the special value?
     *
     * @param key The special object type
     * @return True is this is object the special value?
     */
    boolean isValue(Integer key);

    /**
     * Get all the special values for this object type
     *
     * @return All the special values for this object type
     */
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 23)
    List<Integer> getKeys();
}
