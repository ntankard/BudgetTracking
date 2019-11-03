package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.ArrayList;
import java.util.List;

public class TypeMap extends Container<Class<? extends DataObject>, List<DataObject>> {

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void addType(Class<T> aClass) {
        container.put(aClass, new ArrayList<>());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void add(Class<T> tClass, DataObject dataObject) {
        checkValidType(tClass);

        int startSize = container.get(tClass).size();
        container.get(tClass).add(dataObject);
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
        container.get(dataObject.getTypeClass()).remove(dataObject);
        if (container.get(tClass).size() != startSize - 1)
            throw new RuntimeException("Failed to remove, object probably already removed from the");
    }

    /**
     * Combine all values into one master list
     *
     * @return The list of all values
     */
    public List<DataObject> getAll() {
        List<DataObject> all = new ArrayList<>();
        for (Class aClass : container.keySet()) {
            all.addAll(container.get(aClass));
        }
        return all;
    }
}
