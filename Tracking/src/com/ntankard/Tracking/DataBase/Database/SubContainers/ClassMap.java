package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

public class ClassMap extends Container<String, Class> {

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void addType(Class<T> aClass) {
        container.put(aClass.getSimpleName(), aClass);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void add(Class<T> tClass, DataObject dataObject) {
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void remove(Class<T> tClass, DataObject dataObject) {
    }
}
