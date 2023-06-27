package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;

public class FixedPeriodFundEvent_ElementController extends Database_ElementController<FixedPeriodFundEvent> {

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
