package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

public class ClassMap extends Container<String, Class> {

    /**
     * {@inheritDoc
     */
    @Override
    public void add(DataObject dataObject) {
        if (!container.containsKey(dataObject.getTypeClass().getSimpleName())) {
            container.put(dataObject.getTypeClass().getSimpleName(), dataObject.getTypeClass());
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove(DataObject dataObject) {
        checkCanDelete(dataObject);
    }

    /**
     * Get a value from the container
     *
     * @param key The top level key
     * @return The value
     */
    public Class get(String key) {
        return container.get(key);
    }
}
