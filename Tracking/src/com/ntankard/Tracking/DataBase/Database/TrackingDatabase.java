package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.SubContainers.*;
import com.ntankard.Tracking.Util.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TrackingDatabase {

    // Core data objects
    private List<Container> containers = new ArrayList<>();
    private DefaultObjectMap defaultObjectMap = new DefaultObjectMap();
    private SpecialValuesMap specialValuesMap = new SpecialValuesMap();
    private DataObjectContainer masterMap = new DataObjectContainer();
    private DataObjectClassTree dataObjectClassTree = new DataObjectClassTree();

    // Paths where images can be found
    private String newImagePath;
    private String savedImagePath;

    /**
     * New images to be loaded
     */
    private List<String> possibleImages;

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Constructor ######################################################
    //------------------------------------------------------------------------------------------------------------------

    // Singleton constructor
    private static TrackingDatabase master;

    /**
     * Singleton access
     */
    public static TrackingDatabase get() {
        if (master == null) {
            master = new TrackingDatabase();
        }
        return master;
    }

    /**
     * Reset the singleton and delete all data
     */
    public static void reset() {
        master = null;
    }

    /**
     * Private Constructor
     */
    private TrackingDatabase() {
        containers.add(masterMap);
        containers.add(defaultObjectMap);
        containers.add(specialValuesMap);
        containers.add(dataObjectClassTree);
    }

    /**
     * Finalise the database. From this point on the integrate of the database can be guaranteed and call generated values will exist
     */
    public void finalizeCore() {
        TrackingDatabase_Repair.repair();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Data IO #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the next free ID
     *
     * @return The next free ID
     */
    public Integer getNextId() {
        return masterMap.getNextId();
    }

    /**
     * Add a new element to the database. New elements are repaired if needed and all relevant parents are notified
     *
     * @param dataObject The object to add
     */
    public void add(DataObject dataObject) {
        containers.forEach(container -> container.add(dataObject));
    }

    /**
     * Remove an element from the database as long as it has no children linking to it. All relevant parents are notified
     *
     * @param dataObject The object to remove
     */
    public void remove(DataObject dataObject) {
        containers.forEach(container -> container.remove(dataObject));
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Accessors #######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get an element of the data base based on its unique ID
     *
     * @param type The data type to get
     * @param id   The ID of the object to get
     * @param <T>  The data type to return (same as type)
     * @return The element of the Database
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T get(Class<T> type, Integer id) {
        return masterMap.get(type, id);
    }

    /**
     * Get all elements of the database of a certain type
     *
     * @param type The data type to get
     * @param <T>  The data type to return (same as type)
     * @return A unmodifiableList of all elements of that type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> get(Class<T> type) {
        return masterMap.get(type);
    }

    /**
     * Get all the DataObject types added to this database
     *
     * @return All the DataObject types added to this database
     */
    public Set<Class<? extends DataObject>> getDataObjectTypes() {
        return masterMap.keySet();
    }

    /**
     * Combine all values into one master list
     *
     * @return The list of all values
     */
    public List<DataObject> getAll() {
        return Collections.unmodifiableList(masterMap.get());
    }

    /**
     * Get the default object of a type (specified or the 0th element)
     *
     * @param aClass The class to get
     * @param <T>    The data type to return (same as aClass)
     * @return The default object
     */
    public <T extends DataObject> T getDefault(Class<T> aClass) {
        return defaultObjectMap.getDefault(aClass);
    }

    /**
     * Get the object that is the special value
     *
     * @param aClass The type of object to search
     * @param key    The key to search
     * @return The value of type aClass that is the special value for the key
     */
    public <T extends DataObject> T getSpecialValue(Class<T> aClass, Integer key) {
        return specialValuesMap.get(aClass, key);
    }

    /**
     * Get the root node to the object inheritance tree
     *
     * @return The root node to the object inheritance tree
     */
    public TreeNode<Class<? extends DataObject>> getClassTreeRoot() {
        return dataObjectClassTree.getClassTreeRoot();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Image access #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<String> getPossibleImages() {
        return possibleImages;
    }

    public void setPossibleImages(List<String> possibleImages) {
        this.possibleImages = possibleImages;
    }

    public String getNewImagePath() {
        return newImagePath;
    }

    public void setNewImagePath(String newImagePath) {
        this.newImagePath = newImagePath;
    }

    public String getSavedImagePath() {
        return savedImagePath;
    }

    public void setSavedImagePath(String savedImagePath) {
        this.savedImagePath = savedImagePath;
    }
}
