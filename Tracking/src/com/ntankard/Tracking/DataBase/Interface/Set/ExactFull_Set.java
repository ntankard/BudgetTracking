package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ExactFull_Set<T extends DataObject> extends Full_Set<T> {

    /**
     * Constructor
     */
    public ExactFull_Set(Class<T> tClass) {
        super(tClass);
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
