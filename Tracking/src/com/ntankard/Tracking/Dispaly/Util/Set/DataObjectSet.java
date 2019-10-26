package com.ntankard.Tracking.Dispaly.Util.Set;

import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.util.List;

public interface DataObjectSet<T extends DataObject> {

    /**
     * Get the set of data objects
     *
     * @return A set of daa objects
     */
    List<T> get();
}
