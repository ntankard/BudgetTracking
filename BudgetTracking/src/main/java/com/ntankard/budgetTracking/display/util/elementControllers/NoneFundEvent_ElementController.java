package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.NoneFundEvent;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;

public class NoneFundEvent_ElementController extends Database_ElementController<NoneFundEvent> {

    /**
     * Constructor
     */
    public NoneFundEvent_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public NoneFundEvent newElement() {
        return new NoneFundEvent(
                "",
                getTrackingDatabase().getDefault(SolidCategory.class), false);
    }
}
