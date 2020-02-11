package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.RecurringPayment.Fixed.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

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
        return new FixedRecurringPayment(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                TrackingDatabase.get().getDefault(ExistingPeriod.class),
                null,
                TrackingDatabase.get().getDefault(Bank.class),
                TrackingDatabase.get().getDefault(Category.class));
    }
}
