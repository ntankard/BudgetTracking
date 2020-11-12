package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

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
