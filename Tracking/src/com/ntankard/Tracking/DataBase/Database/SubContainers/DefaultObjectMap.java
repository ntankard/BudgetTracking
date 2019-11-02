package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

public class DefaultObjectMap extends Container<Class, DataObject> {

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void addType(Class<T> aClass) {
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void add(Class<T> tClass, DataObject dataObject) {
        if (HasDefault.class.isAssignableFrom(tClass)) {
            if (((HasDefault) dataObject).isDefault()) {
                if (container.containsKey(tClass)) {
                    throw new RuntimeException("Default already set");
                }
                container.put(tClass, dataObject);
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void remove(Class<T> tClass, DataObject dataObject) {
        checkCanDelete(dataObject);

        if (container.containsKey(dataObject.getTypeClass())) {
            if (container.get(dataObject.getTypeClass()).equals(dataObject)) {
                container.remove(dataObject.getTypeClass());
            }
        }
    }

    /**
     * Get the default value that should be used for a specific object type
     *
     * @param aClass The type to get
     * @param <T>    The type, same as aClass
     * @return The default value that should be used for a specific object type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T getDefault(Class<T> aClass) {
        if (container.containsKey(aClass)) {
            return (T) container.get(aClass);
        }
        return TrackingDatabase.get().get(aClass).get(0);
    }
}
