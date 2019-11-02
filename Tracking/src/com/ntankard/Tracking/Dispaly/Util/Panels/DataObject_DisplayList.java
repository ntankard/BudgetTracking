package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.CurrencyBound_LocaleSource;
import com.ntankard.Tracking.DataBase.Interface.Set.DataObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;

import java.util.ArrayList;

public class DataObject_DisplayList<T extends DataObject> extends DynamicGUI_DisplayList<T> {

    /**
     * The source of data for the list
     */
    private DataObjectSet<T> set;

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
     * Constructor. Build a list from a set of type tClass. Filter added by default
     *
     * @param tClass The type of object to display
     * @param set    The source of data to display
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, DataObjectSet<T> set, Updatable master) {
        this(tClass, set, true, master);
    }

    /**
     * Constructor. Build a list from a set of type tClass. Filter added by default
     *
     * @param tClass The type of object to display
     * @param set    The source of data to display
     * @param filter Should a filter be added
     * @param master The parent to notify if data changes
     */
    public DataObject_DisplayList(Class<T> tClass, DataObjectSet<T> set, boolean filter, Updatable master) {
        super(new ArrayList<>(), new MemberClass(tClass), master);
        this.set = set;
        if (CurrencyBound.class.isAssignableFrom(tClass)) {
            setLocaleSource(new CurrencyBound_LocaleSource());
        }
        setSources(TrackingDatabase.get());
        if (filter) {
            addFilter();
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        base.clear();
        base.addAll(set.get());
        super.update();
    }
}
