package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Container<K, V> {

    protected Map<K, V> container = new HashMap<>();

    /**
     * Generate a container for a new object type
     *
     * @param aClass The type to add
     * @param <T>    Same as type
     */
    public abstract <T extends DataObject> void addType(Class<T> aClass);

    /**
     * Add an element to the container
     *
     * @param tClass     The type of element (the class to group it under, not necessarily the lowest child class)
     * @param dataObject The object to add
     * @param <T>        The type, same as tClass
     */
    public abstract <T extends DataObject> void add(Class<T> tClass, DataObject dataObject);

    /**
     * Remove an element from the container
     *
     * @param tClass     The type of element (the class to group it under, not necessarily the lowest child class)
     * @param dataObject The object to remove
     * @param <T>        The type, same as tClass
     */
    public abstract <T extends DataObject> void remove(Class<T> tClass, DataObject dataObject);

    /**
     * Get a value from the container
     *
     * @param key The top level key
     * @return The value
     */
    public V get(K key) {
        return container.get(key);
    }

    /**
     * Dose the container contain this top level key
     *
     * @param key The top level jey to check
     * @return True if the container has this key
     */
    public boolean containsKey(K key) {
        return container.containsKey(key);
    }

    /**
     * Throws an exception if the container dose contain the key
     *
     * @param tClass The key to check
     */
    protected void checkValidType(K tClass) {
        if (!containsKey(tClass)) {
            throw new RuntimeException("DataObject is of an unknown type, check that all types have been initialized in the database");
        }
    }

    /**
     * Throws an exception if an object is not safe to delete
     *
     * @param dataObject The object to check
     */
    protected void checkCanDelete(DataObject dataObject) {
        if (dataObject.getChildren().size() != 0) {
            throw new RuntimeException("Deleting an object with dependencies");
        }
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<K> keySet() {
        return container.keySet();
    }
}
