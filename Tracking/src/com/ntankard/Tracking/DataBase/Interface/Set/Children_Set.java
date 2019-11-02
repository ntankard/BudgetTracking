package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.util.ArrayList;
import java.util.List;

public class Children_Set<T extends DataObject, CoreType extends DataObject> implements DataObjectSet<T> {

    /**
     * The DataObject to get from the core
     */
    private final Class<T> tClass;

    /**
     * The core object to extract children from
     */
    private CoreType core;

    /**
     * Constructor
     */
    public Children_Set(Class<T> tClass, CoreType core) {
        this.tClass = tClass;
        this.core = core;
    }

    /**
     * Set the core object to extract children from
     *
     * @param core The core object to extract children from
     */
    public void setCore(CoreType core) {
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        if (core != null) {
            return core.getChildren(tClass);
        }
        return new ArrayList<>();
    }
}
