package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.OutCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Currency;
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
        Fund toUse = TrackingDatabase.get().getDefault(Fund.class);
        for (Fund fund : TrackingDatabase.get().get(Fund.class)) {
            if (fund.getChildren(FundEvent.class).size() != 0) {
                toUse = fund;
                break;
            }
        }

        return new CategoryFundTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(OutCategory.class),
                toUse,
                toUse.getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
