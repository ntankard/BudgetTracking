package com.ntankard.Tracking.DataBase.Database.SubContainers;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import java.util.*;

public class DataObjectContainer extends Container<Class<? extends DataObject>, Map<Integer, DataObject>> {

    /**
     * The main container
     */
    private Map<Class<? extends DataObject>, Comparator> comparators = new HashMap<>();

    /**
     * Add a comparator to be used when getting a list of a certain type
     *
     * @param aClass     The type to use the comparator on
     * @param comparator The comparator to use
     * @param <T>        The type, same as aClass
     */
    public <T extends DataObject> void addComparator(Class<T> aClass, Comparator<T> comparator) {
        comparators.put(aClass, comparator);
    }

    /**
     * Add a new object
     *
     * @param toAdd the object to add
     */
    @SuppressWarnings("unchecked")
    @Override
    public void add(DataObject toAdd) {
        Class<? extends DataObject> aClass = toAdd.getClass();
        do {
            // Create the class container if it dos'nt already exist
            if (!container.containsKey(aClass)) {
                container.put(aClass, new HashMap<>());
            }

            // Check for duplicate IDs across the entire container
            if (container.get(aClass).containsKey(toAdd.getId())) {
                throw new RuntimeException("Duplicate key found");
            }

            // Add the object at this layer
            container.get(aClass).put(toAdd.getId(), toAdd);

            // Jump up the inheritance tree
            aClass = (Class<? extends DataObject>) aClass.getSuperclass();
        } while (DataObject.class.isAssignableFrom(aClass));
    }

    /**
     * Remove an object
     *
     * @param toRemove The object to remove
     */
    @SuppressWarnings("unchecked")
    @Override
    public void remove(DataObject toRemove) {
        checkCanDelete(toRemove);

        Class<? extends DataObject> aClass = toRemove.getClass();
        do {
            // Check that we have seen this object before
            if (!container.containsKey(aClass) || !container.get(aClass).containsKey(toRemove.getId())) {
                System.out.println("Removing an object that was never added, TODO create error");
                //throw new RuntimeException("Removing an object that was never added");
            }

            // Remove the object
            container.get(aClass).remove(toRemove.getId());

            // Jump up the inheritance tree
            aClass = (Class<? extends DataObject>) aClass.getSuperclass();
        } while (DataObject.class.isAssignableFrom(aClass));
    }

    /**
     * Get all object in the container
     *
     * @return All the objects in the containers
     */
    public List<DataObject> get() {
        return get(DataObject.class);
    }

    /**
     * Get all object of, or extending a certain class
     *
     * @param tClass The object to get
     * @param <T>    Type, same as tClass
     * @return All object that are, or inherit from tClass
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> get(Class<T> tClass) {
        if (!container.containsKey(tClass)) {
            if (tClass == null) {
                throw new RuntimeException("Trying to get a null item");
            }
            container.put(tClass, new HashMap<>());
        }
        List toReturn = new ArrayList(container.get(tClass).values());
        if (comparators.containsKey(tClass)) {
            toReturn.sort(comparators.get(tClass));
        }
        return toReturn;
    }

    /**
     * Get a specific object from the container
     *
     * @param tClass The type of the object
     * @param id     The ID to get
     * @param <T>    Type, same as tClass
     * @return The specific object or null
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T get(Class<T> tClass, Integer id) {
        if (!container.containsKey(tClass)) {
            if (tClass == null) {
                throw new RuntimeException("Trying to get a null item");
            }
            container.put(tClass, new HashMap<>());
        }
        return (T) container.get(tClass).get(id);
    }

    /**
     * Get thee next ID that will not cause a conflict with any elements in the container
     *
     * @return The next ID that will not cause a conflict with any elements in the container
     */
    public Integer getNextId() {
        int max = 0;
        for (DataObject dataObject_new : get()) {
            int value = dataObject_new.getId();
            if (value > max) {
                max = value;
            }
        }
        return (max + 1);
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<Class<? extends DataObject>> keySet() {
        return container.keySet();
    }
}
