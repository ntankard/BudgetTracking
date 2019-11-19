package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ExactMultiParent_Set<T extends DataObject, PrimaryParentType extends DataObject, SecondaryParentType extends DataObject> extends MultiParent_Set<T, PrimaryParentType, SecondaryParentType> {

    /**
     * Constructor
     */
    public ExactMultiParent_Set(Class<T> tClass, PrimaryParentType primaryParent, SecondaryParentType secondaryParent) {
        super(tClass, primaryParent, secondaryParent);
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
