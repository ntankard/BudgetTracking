package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.UseCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class UseCategoryFundTransfer_ElementController extends TrackingDatabase_ElementController<UseCategoryFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public UseCategoryFundTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public UseCategoryFundTransfer newElement() {
        return new UseCategoryFundTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(Category.class).getChildren(Fund.class).get(0).getDefaultFundEvent(),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
