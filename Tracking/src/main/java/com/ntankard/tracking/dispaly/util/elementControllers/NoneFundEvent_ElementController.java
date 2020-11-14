package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.NoneFundEvent;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class NoneFundEvent_ElementController extends TrackingDatabase_ElementController<NoneFundEvent> {

    /**
     * Constructor
     */
    public NoneFundEvent_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public NoneFundEvent newElement() {
        return NoneFundEvent.make(getTrackingDatabase().getNextId(),
                "",
                getTrackingDatabase().getDefault(SolidCategory.class));
    }
}
