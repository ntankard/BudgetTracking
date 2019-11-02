package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.SpecialValues;

import java.util.HashMap;
import java.util.Map;

public class SpecialValuesMap extends Container<Class, Map<Integer, DataObject>> {

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void addType(Class<T> aClass) {
        if (SpecialValues.class.isAssignableFrom(aClass)) {
            container.put(aClass, new HashMap<>());
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void add(Class<T> tClass, DataObject dataObject) {
        if (dataObject instanceof SpecialValues) {
            for (Integer key : ((SpecialValues) dataObject).getKeys()) {
                if (((SpecialValues) dataObject).isValue(key)) {
                    Map<Integer, DataObject> keyMap = container.get(tClass);
                    if (keyMap.containsKey(key)) {
                        throw new RuntimeException("Double add");
                    }
                    keyMap.put(key, dataObject);
                }
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public <T extends DataObject> void remove(Class<T> tClass, DataObject dataObject) {
        if (dataObject instanceof SpecialValues) {
            for (Integer key : ((SpecialValues) dataObject).getKeys()) {
                if (((SpecialValues) dataObject).isValue(key)) {
                    Map<Integer, DataObject> keyMap = container.get(tClass);
                    if (!keyMap.containsKey(key)) {
                        throw new RuntimeException("Removing a value that dose not exist");
                    }
                    keyMap.remove(key);
                }
            }
        }
    }

    /**
     * Get the object that is the special value
     *
     * @param aClass The type of object to search
     * @param key    The key to search
     * @return The value of type aClass that is the special value for the key
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T get(Class<T> aClass, Integer key) {
        return (T) container.get(aClass).get(key);
    }
}
