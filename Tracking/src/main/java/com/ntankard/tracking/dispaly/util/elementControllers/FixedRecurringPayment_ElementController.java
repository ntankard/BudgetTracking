package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class FixedRecurringPayment_ElementController extends TrackingDatabase_ElementController<FixedRecurringPayment> {

    /**
     * Constructor
     */
    public FixedRecurringPayment_ElementController(TrackingDatabase trackingDatabase, Updatable master) {
        super(trackingDatabase, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FixedRecurringPayment newElement() {
        return FixedRecurringPayment.make(getTrackingDatabase().getNextId(),
                "",
                0.0,
                getTrackingDatabase().getDefault(ExistingPeriod.class),
                getTrackingDatabase().getDefault(Bank.class),
                getTrackingDatabase().getDefault(SolidCategory.class),
                null);
    }
}
