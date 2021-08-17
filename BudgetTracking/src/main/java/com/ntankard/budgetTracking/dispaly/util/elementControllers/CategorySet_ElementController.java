package com.ntankard.budgetTracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.budgetTracking.dataBase.core.CategorySet;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class CategorySet_ElementController extends TrackingDatabase_ElementController<CategorySet> {

    /**
     * Constructor
     */
    public CategorySet_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public CategorySet newElement() {
        return new CategorySet(getTrackingDatabase(),
                "",
                false,
                0);
    }
}
