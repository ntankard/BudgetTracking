package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.NoneFundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class NoneFundEvent_ElementController extends TrackingDatabase_ElementController<NoneFundEvent> {

    /**
     * Constructor
     */
    public NoneFundEvent_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public NoneFundEvent newElement() {
        return NoneFundEvent.make(TrackingDatabase.get().getNextId(),
                "",
                TrackingDatabase.get().getDefault(Category.class));
    }
}
