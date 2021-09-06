package com.ntankard.budgetTracking.display.util.panels;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

import java.awt.*;

public class DataObject_DisplayList<T extends DataObject> extends Object_DisplayList<T> {

    /**
     * Constructor. Build a list for all object of type tClass. Filter added by default
     *
     * @param tClass The type of object to display
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Database database, Class<T> tClass, Updatable master) {
        this(database.getSchema(), tClass, new Full_Set<>(database, tClass), master);
    }

    /**
     * Constructor. Build a list for all object of type tClass
     *
     * @param tClass The type of object to display
     * @param filter Should a filter be added
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Database database, Class<T> tClass, boolean filter, Updatable master) {
        this(database.getSchema(), tClass, new Full_Set<>(database, tClass), filter, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public DataObject_DisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
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
    public DataObject_DisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(schema, tClass, objectSet, filter, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension();
    }
}
