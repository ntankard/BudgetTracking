package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class PeriodFundTransfer_ElementController extends TrackingDatabase_ElementController<PeriodFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public PeriodFundTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public PeriodFundTransfer newElement() {
        Fund toUse = TrackingDatabase.get().getDefault(Fund.class);
        for (Fund fund : TrackingDatabase.get().get(Fund.class)) {
            if (fund.getChildren(FundEvent.class).size() != 0) {
                toUse = fund;
                break;
            }
        }

        return new PeriodFundTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(Category.class),
                toUse,
                toUse.getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
