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
    public FixedRecurringPayment_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FixedRecurringPayment newElement() {
        return FixedRecurringPayment.make(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                TrackingDatabase.get().getDefault(ExistingPeriod.class),
                TrackingDatabase.get().getDefault(Bank.class),
                TrackingDatabase.get().getDefault(SolidCategory.class),
                null);
    }
}
