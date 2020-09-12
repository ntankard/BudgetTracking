package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategorySet_ElementController extends TrackingDatabase_ElementController<CategorySet> {

    /**
     * Constructor
     */
    public CategorySet_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategorySet newElement() {
        return CategorySet.make(TrackingDatabase.get().getNextId(),
                "",
                false,
                0);
    }
}
