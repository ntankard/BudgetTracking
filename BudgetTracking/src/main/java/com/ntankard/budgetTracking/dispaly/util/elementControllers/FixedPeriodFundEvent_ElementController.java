package com.ntankard.budgetTracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class FixedPeriodFundEvent_ElementController extends TrackingDatabase_ElementController<FixedPeriodFundEvent> {

    /**
     * Constructor
     */
    public FixedPeriodFundEvent_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public FixedPeriodFundEvent newElement() {
        return new FixedPeriodFundEvent(
                "",
                getTrackingDatabase().getDefault(SolidCategory.class),
                getTrackingDatabase().getDefault(ExistingPeriod.class),
                1,
                false);
    }
}
