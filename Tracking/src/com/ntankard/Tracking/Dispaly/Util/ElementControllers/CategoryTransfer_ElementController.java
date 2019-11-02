package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategoryTransfer_ElementController extends TrackingDatabase_ElementController<CategoryTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public CategoryTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryTransfer newElement() {
        return new CategoryTransfer(TrackingDatabase.get().getNextId(CategoryTransfer.class),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(Category.class),
                TrackingDatabase.get().getDefault(Category.class),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
