package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.List;

public interface DataObjectSet<T extends DataObject> {

    /**
     * Get the set of data objects
     *
     * @return A set of daa objects
     */
    List<T> get();
}
