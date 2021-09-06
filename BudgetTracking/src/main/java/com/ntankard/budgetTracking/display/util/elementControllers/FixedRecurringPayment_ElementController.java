package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;

public class FixedRecurringPayment_ElementController extends Database_ElementController<FixedRecurringPayment> {

    /**
     * Constructor
     */
    public FixedRecurringPayment_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public FixedRecurringPayment newElement() {
        return new FixedRecurringPayment(
                "",
                0.0,
                getTrackingDatabase().getDefault(ExistingPeriod.class),
                getTrackingDatabase().getDefault(Bank.class),
                getTrackingDatabase().getDefault(SolidCategory.class),
                null);
    }
}
