package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataObject {

    /**
     * All my children sorted by class
     */
    private Map<Class, List<DataObject>> children = new HashMap<>();

    /**
     * Get all the parents of this object
     *
     * @return All the parents of this object
     */
    public abstract List<DataObject> getParents();

    /**
     * Get the unique identifier for this data object
     *
     * @return The unique identifier for this data object
     */
    public abstract String getId();

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    /**
     * Notify all the objects parents that this object has linked to them
     */
    public void notifyParentLink() {
        for (DataObject dataObject : getParents()) {
            dataObject.notifyChildLink(this);
        }
    }

    /**
     * Notify all this object's parent that this object is removing the link to them. Call before deleting this object
     */
    public void notifyParentUnLink() {
        for (DataObject dataObject : getParents()) {
            dataObject.notifyChildUnLink(this);
        }
    }

    /**
     * Notify that a child has linked to this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildLink(DataObject linkObject) {
        Class classType = linkObject.getClass();
        if (!children.containsKey(classType)) {
            children.put(classType, new ArrayList<>());
        }

        List<DataObject> classChildren = children.get(classType);
        if (!classChildren.contains(linkObject)) {
            classChildren.add(linkObject);
        }
    }

    /**
     * Notify that a child has un linked this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildUnLink(DataObject linkObject) {
        Class classType = linkObject.getClass();
        if (!children.containsKey(classType)) {
            return;
        }

        children.get(classType).remove(linkObject);
    }

    /**
     * Get the list of children for a a specific class type
     *
     * @param type The class type to get
     * @param <T>  The Object type
     * @return The list of children for a a specific class type
     */
    public <T extends DataObject> List<T> getChildren(Class type) {
        if (!children.containsKey(type)) {
            children.put(type, new ArrayList<>());
        }

        return (List<T>) children.get(type);
    }
}
