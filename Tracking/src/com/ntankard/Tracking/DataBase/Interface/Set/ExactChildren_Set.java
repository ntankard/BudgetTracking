package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ExactChildren_Set<T extends DataObject, ParentType extends DataObject> extends Children_Set<T, ParentType> {

    /**
     * Constructor
     */
    public ExactChildren_Set(Class<T> tClass, ParentType parent) {
        super(tClass, parent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        List<T> toReturn = new ArrayList<>();
        for (T dataObject : super.get()) {
            if (dataObject.getClass().equals(tClass)) {
                toReturn.add(dataObject);
            }
        }
        return toReturn;
    }
}
