package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class FixedPeriodFundEvent_ElementController extends TrackingDatabase_ElementController<FixedPeriodFundEvent> {

    /**
     * Constructor
     */
    public FixedPeriodFundEvent_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FixedPeriodFundEvent newElement() {
        return new FixedPeriodFundEvent(TrackingDatabase.get().getNextId(),
                "",
                TrackingDatabase.get().getDefault(Category.class),
                TrackingDatabase.get().getDefault(Period.class),
                1);
    }
}
