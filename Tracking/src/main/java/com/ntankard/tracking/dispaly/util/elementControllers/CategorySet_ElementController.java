package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class CategorySet_ElementController extends TrackingDatabase_ElementController<CategorySet> {

    /**
     * Constructor
     */
    public CategorySet_ElementController(TrackingDatabase trackingDatabase, Updatable master) {
        super(trackingDatabase, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategorySet newElement() {
        return CategorySet.make(getTrackingDatabase(), getTrackingDatabase().getNextId(),
                "",
                false,
                0);
    }
}
