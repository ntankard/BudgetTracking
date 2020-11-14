package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

public class DataObject_DisplayList<T extends DataObject> extends Object_DisplayList<T> {

    /**
     * Constructor. Build a list for all object of type tClass. Filter added by default
     *
     * @param tClass The type of object to display
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(TrackingDatabase trackingDatabase, Class<T> tClass, Updatable master) {
        this(trackingDatabase.getSchema(), tClass, new Full_Set<>(trackingDatabase, tClass), master);
    }

    /**
     * Constructor. Build a list for all object of type tClass
     *
     * @param tClass The type of object to display
     * @param filter Should a filter be added
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(TrackingDatabase trackingDatabase, Class<T> tClass, boolean filter, Updatable master) {
        this(trackingDatabase.getSchema(), tClass, new Full_Set<>(trackingDatabase, tClass), filter, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public DataObject_DisplayList(TrackingDatabase_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        super(schema, tClass, objectSet, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param filter    Should a filter be added
     * @param master    The parent to notify if data changes
     */
    public DataObject_DisplayList(TrackingDatabase_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(schema, tClass, objectSet, filter, master);
    }
}
