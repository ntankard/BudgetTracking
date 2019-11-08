package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.HashMap;
import java.util.Map;

public class TypeIDMap extends Container<Class, Map<Integer, DataObject>> {

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void addType(Class<T> aClass) {
        container.put(aClass, new HashMap<>());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void add(Class<T> tClass, DataObject dataObject) {
        checkValidType(tClass);

        int startSize = container.get(tClass).size();
        this.container.get(tClass).put(dataObject.getId(), dataObject);
        if (container.get(tClass).size() != startSize + 1)
            throw new RuntimeException("Failed to add, object probably already exists in the database");
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void remove(Class<T> tClass, DataObject dataObject) {
        checkValidType(tClass);
        checkCanDelete(dataObject);

        int startSize = container.get(tClass).size();
        this.container.get(dataObject.getTypeClass()).remove(dataObject.getId());
        if (container.get(tClass).size() != startSize - 1)
            throw new RuntimeException("Failed to add, object probably already exists in the database");
    }
}
