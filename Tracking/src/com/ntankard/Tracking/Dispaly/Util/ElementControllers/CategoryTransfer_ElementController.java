package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategoryTransfer_ElementController extends TrackingDatabase_ElementController<CategoryTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;

    /**
     * Constructor
     */
    public CategoryTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryTransfer newElement() {
        return new CategoryTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                core,
                TrackingDatabase.get().get(Category.class).get(0),
                TrackingDatabase.get().get(Category.class).get(1),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
