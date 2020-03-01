package com.ntankard.Tracking.DataBase.Interface.Set;

import java.util.List;

public class Array_Set<T> extends ObjectSet<T> {

    /**
     * The data to return
     */
    private List<T> set;

    /**
     * Constructor
     */
    public Array_Set(List<T> set) {
        super(null);
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
