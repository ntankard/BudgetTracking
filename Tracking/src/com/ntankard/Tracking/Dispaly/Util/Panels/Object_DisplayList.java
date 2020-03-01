package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.CurrencyBound_LocaleSource;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.HalfTransfer_LocaleSource;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.SetTransfer_LocaleSource;

import java.util.ArrayList;

public class Object_DisplayList<T> extends DynamicGUI_DisplayList<T> {

    /**
     * The source of data for the list
     */
    private ObjectSet<T> objectSet;

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public Object_DisplayList(Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        this(tClass, objectSet, true, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param filter    Should a filter be added
     * @param master    The parent to notify if data changes
     */
    public Object_DisplayList(Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(new ArrayList<>(), new MemberClass(tClass), master);
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
