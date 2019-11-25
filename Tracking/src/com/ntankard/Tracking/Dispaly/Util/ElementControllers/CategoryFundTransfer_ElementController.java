package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.OutCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategoryFundTransfer_ElementController extends TrackingDatabase_ElementController<CategoryFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public CategoryFundTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryFundTransfer newElement() {
        return new CategoryFundTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(OutCategory.class),
                TrackingDatabase.get().getDefault(OutCategory.class).getChildren(Fund.class).get(0).getDefaultFundEvent(),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
