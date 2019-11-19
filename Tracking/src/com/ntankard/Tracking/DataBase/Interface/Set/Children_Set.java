package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class Children_Set<T extends DataObject, ParentType extends DataObject> implements ObjectSet<T> {

    /**
     * The DataObject to get from the core
     */
    protected final Class<T> tClass;

    /**
     * The core object to extract children from
     */
    protected ParentType parent;

    /**
     * Constructor
     */
    public Children_Set(Class<T> tClass, ParentType parent) {
        this.tClass = tClass;
        this.parent = parent;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        if (parent != null) {
            return parent.getChildren(tClass);
        }
        return new ArrayList<>();
    }

    /**
     * Set the core object to extract children from
     *
     * @param parent The core object to extract children from
     */
    public void setParent(ParentType parent) {
        this.parent = parent;
    }
}
