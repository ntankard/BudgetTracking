package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class FixedPeriodFundEvent_ElementController extends TrackingDatabase_ElementController<FixedPeriodFundEvent> {

    /**
     * Constructor
     */
    public FixedPeriodFundEvent_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FixedPeriodFundEvent newElement() {
        return FixedPeriodFundEvent.make(getTrackingDatabase().getNextId(),
                "",
                getTrackingDatabase().getDefault(SolidCategory.class),
                getTrackingDatabase().getDefault(ExistingPeriod.class),
                1);
    }
}
