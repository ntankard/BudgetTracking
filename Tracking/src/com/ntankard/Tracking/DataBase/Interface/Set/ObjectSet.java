package com.ntankard.Tracking.DataBase.Interface.Set;

import java.util.List;

public interface ObjectSet<T> {

    /**
     * Get the set of objects
     *
     * @return A set of daa objects
     */
    List<T> get();
}
