package com.ntankard.Tracking.DataBase.Interface.Set;

import java.util.List;

public class Array_Set<T> implements ObjectSet<T> {

    /**
     * The data to return
     */
    private List<T> set;

    /**
     * Constructor
     */
    public Array_Set(List<T> set) {
        this.set = set;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        return set;
    }
}
