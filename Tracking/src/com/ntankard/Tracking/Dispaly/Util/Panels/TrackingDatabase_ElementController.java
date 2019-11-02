package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

public abstract class TrackingDatabase_ElementController<T extends DataObject> implements ElementController<T> {

    /**
     * The parent to notify when data changes
     */
    private Updatable master;

    /**
     * Constructor
     */
    public TrackingDatabase_ElementController(Updatable master) {
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(T toDel) {
        TrackingDatabase.get().remove(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(T newObj) {
        TrackingDatabase.get().add(newObj);
        master.notifyUpdate();
    }
}