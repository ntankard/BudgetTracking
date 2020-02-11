package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.ClassExtension.ClassExtensionProperties;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public interface SpecialValues {

    /**
     * Is this object the special value?
     *
     * @param key The special object type
     * @return True is this is object the special value?
     */
    Boolean isValue(Integer key);

    /**
     * Get all the special values for this object type
     *
     * @return All the special values for this object type
     */
    List<Integer> getKeys();
}
