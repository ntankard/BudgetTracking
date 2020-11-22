package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;
import com.ntankard.tracking.dispaly.util.localeInspectors.CurrencyBound_LocaleSource;
import com.ntankard.tracking.dispaly.util.localeInspectors.HalfTransfer_LocaleSource;
import com.ntankard.tracking.dispaly.util.localeInspectors.SetTransfer_LocaleSource;
import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_DisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;

import java.util.ArrayList;

public class Object_DisplayList<T extends DataObject> extends DynamicGUI_DisplayList<T> {

    /**
     * The source of data for the list
     */
    private final ObjectSet<T> objectSet;

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public Object_DisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        this(schema, tClass, objectSet, true, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param filter    Should a filter be added
     * @param master    The parent to notify if data changes
     */
    public Object_DisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(schema, new ArrayList<>(), tClass, master);
        this.objectSet = objectSet;
        if (Transfer.class.isAssignableFrom(tClass)) {
            setLocaleSource(new SetTransfer_LocaleSource());
        } else if (HalfTransfer.class.isAssignableFrom(tClass)) {
            setLocaleSource(new HalfTransfer_LocaleSource());
        } else if (CurrencyBound.class.isAssignableFrom(tClass)) {
            setLocaleSource(new CurrencyBound_LocaleSource());
        }
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
        base.addAll(objectSet.get());
        super.update();
    }
}
