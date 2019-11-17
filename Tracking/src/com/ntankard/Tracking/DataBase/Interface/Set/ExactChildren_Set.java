package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ExactChildren_Set<T extends DataObject, CoreType extends DataObject> implements ObjectSet<T> {

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
    public ExactChildren_Set(Class<T> tClass, CoreType core) {
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
        List<T> toReturn = new ArrayList<>();
        for (T t : core.getChildren(tClass)) {
            if (t.getClass().equals(tClass)) {
                toReturn.add(t);
            }
        }
        return toReturn;
    }
}
