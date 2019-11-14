package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.SubContainers.DataObjectContainer;

import java.util.*;

import static com.ntankard.ClassExtension.MemberProperties.*;

public abstract class DataObject {

    // My values
    private Integer id;

    /**
     * All my children
     */
    private DataObjectContainer container = new DataObjectContainer();

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
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
        } catch (Exception e) {
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
        container.add(linkObject);
    }

    /**
     * Notify that a child has un linked this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildUnLink(DataObject linkObject) {
        container.remove(linkObject);
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
        return container.get(type);
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
        return container.get(type, key);
    }

    /**
     * Get the list of all children
     *
     * @return The list of all children
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 22)
    public List<DataObject> getChildren() {
        return container.get();
    }
}
