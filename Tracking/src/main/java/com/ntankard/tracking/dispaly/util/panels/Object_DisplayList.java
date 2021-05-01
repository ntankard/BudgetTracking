package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_SetDisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;
import com.ntankard.tracking.dispaly.util.localeInspectors.CurrencyBound_LocaleSource;
import com.ntankard.tracking.dispaly.util.localeInspectors.HalfTransfer_LocaleSource;
import com.ntankard.tracking.dispaly.util.localeInspectors.SetTransfer_LocaleSource;

public class Object_DisplayList<T extends DataObject> extends DynamicGUI_SetDisplayList<T> {

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
        super(schema, tClass, objectSet, filter, master);

        if (Transfer.class.isAssignableFrom(tClass)) {
            setLocaleSource(new SetTransfer_LocaleSource());
        } else if (HalfTransfer.class.isAssignableFrom(tClass)) {
            setLocaleSource(new HalfTransfer_LocaleSource());
        } else if (CurrencyBound.class.isAssignableFrom(tClass)) {
            setLocaleSource(new CurrencyBound_LocaleSource());
        }
    }
}
