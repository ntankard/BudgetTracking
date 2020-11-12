package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

public class DataObject_DisplayList<T extends DataObject> extends Object_DisplayList<T> {

    /**
     * Constructor. Build a list for all object of type tClass. Filter added by default
     *
     * @param tClass The type of object to display
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, Updatable master) {
        this(tClass, new Full_Set<>(tClass), master);
    }

    /**
     * Constructor. Build a list for all object of type tClass
     *
     * @param tClass The type of object to display
     * @param filter Should a filter be added
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, boolean filter, Updatable master) {
        this(tClass, new Full_Set<>(tClass), filter, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        super(tClass, objectSet, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param filter    Should a filter be added
     * @param master    The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(tClass, objectSet, filter, master);
    }
}
