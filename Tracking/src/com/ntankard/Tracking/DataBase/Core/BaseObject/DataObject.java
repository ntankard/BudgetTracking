package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;

import java.util.*;

import static com.ntankard.ClassExtension.MemberProperties.*;

public abstract class DataObject {

    // My values
    private Integer id;

    /**
     * All my children sorted by class
     */
    private Map<Class, List<DataObject>> children = new HashMap<>();

    /**
     * All my children sorted by class, and mapped to there data object ID
     */
    private Map<Class, Map<Integer, DataObject>> childrenMap = new HashMap<>();

    /**
     * Constructor
     */
    public DataObject(Integer id) {
        this.id = id;
    }

    /**
     * Get the unique identifier for this data object
     *
     * @return The unique identifier for this data object
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1)
    public Integer getId() {
        return id;
    }

    /**
     * The class used to group these objects
     *
     * @return The class used to group these objects
     */
    @MemberProperties(verbosityLevel = MemberProperties.DEBUG_DISPLAY)
    @DisplayProperties(order = 20)
    public Class<?> getTypeClass() {
        return getClass();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId().toString();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Parental Hierarchy  ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Notify all the objects parents that this object has linked to them
     */
    public void notifyParentLink() {
        try {


            for (DataObject dataObject : getParents()) {
                dataObject.notifyChildLink(this);
            }
        }catch (Exception e){
            e.printStackTrace();
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
     * Get all the parents of this object
     *
     * @return All the parents of this object
     */
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public abstract List<DataObject> getParents();

    /**
     * Notify that a child has linked to this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildLink(DataObject linkObject) {
        Class classType = linkObject.getTypeClass();
        if (!children.containsKey(classType)) {
            children.put(classType, new ArrayList<>());
            childrenMap.put(classType, new HashMap<>());
        }

        List<DataObject> classChildren = children.get(classType);

        if (!classChildren.contains(linkObject)) {
            classChildren.add(linkObject);
        }

        Map<Integer, DataObject> classChildrenMap = childrenMap.get(classType);
        if (!classChildrenMap.containsKey(linkObject.getId())) {
            classChildrenMap.put(linkObject.getId(), linkObject);
        }
    }

    /**
     * Notify that a child has un linked this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildUnLink(DataObject linkObject) {
        Class classType = linkObject.getTypeClass();
        if (!children.containsKey(classType)) {
            return;
        }

        childrenMap.get(classType).remove(linkObject.getId());
        children.get(classType).remove(linkObject);
    }

    /**
     * Get the list of children for a a specific class type
     *
     * @param type The class type to get
     * @param <T>  The Object type
     * @return The list of children for a a specific class type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> getChildren(Class<T> type) {
        if (!children.containsKey(type)) {
            children.put(type, new ArrayList<>());
            childrenMap.put(type, new HashMap<>());
        }

        return (List<T>) children.get(type);
    }

    /**
     * Get the list of children for a a specific class type
     *
     * @param type The class type to get
     * @param key  The ID of the object to get
     * @param <T>  The Object type
     * @return The list of children for a a specific class type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T getChildren(Class<T> type, Integer key) {
        if (!childrenMap.containsKey(type)) {
            children.put(type, new ArrayList<>());
            childrenMap.put(type, new HashMap<>());
        }

        return (T) childrenMap.get(type).get(key);
    }

    /**
     * Get the list of all children
     *
     * @return The list of all children
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 22)
    public List<DataObject> getChildren() {
        List<DataObject> toReturn = new ArrayList<>();
        for (Class aClass : children.keySet()) {
            toReturn.addAll(children.get(aClass));
        }
        return toReturn;
    }
}
