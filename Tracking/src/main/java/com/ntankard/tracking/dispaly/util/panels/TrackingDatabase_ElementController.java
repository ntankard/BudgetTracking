package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

public abstract class TrackingDatabase_ElementController<T extends DataObject> implements ElementController<T> {

    /**
     * The core database that the element will be entered in
     */
    private final TrackingDatabase trackingDatabase;

    /**
     * The parent to notify when data changes
     */
    private final Updatable master;

    /**
     * Constructor
     */
    public TrackingDatabase_ElementController(TrackingDatabase trackingDatabase, Updatable master) {
        this.trackingDatabase = trackingDatabase;
        this.master = master;
    }

    /**
     * Get the core database that the element will be entered in
     *
     * @return The core database that the element will be entered in
     */
    protected TrackingDatabase getTrackingDatabase() {
        return trackingDatabase;
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
