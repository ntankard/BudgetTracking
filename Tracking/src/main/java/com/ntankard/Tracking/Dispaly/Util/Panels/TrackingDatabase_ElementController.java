package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.dynamicGUI.Gui.Containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

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
        toDel.remove();
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(T newObj) {
        newObj.add();
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canCreate() {
        return true;
    }
}
