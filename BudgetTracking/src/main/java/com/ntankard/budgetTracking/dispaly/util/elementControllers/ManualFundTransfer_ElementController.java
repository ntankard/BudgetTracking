package com.ntankard.budgetTracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.ManualFundTransfer;
import com.ntankard.budgetTracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class ManualFundTransfer_ElementController extends TrackingDatabase_ElementController<ManualFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private final Period period;

    /**
     * Constructor
     */
    public ManualFundTransfer_ElementController(Period period, Updatable master) {
        super(period.getTrackingDatabase(), master);
        this.period = period;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ManualFundTransfer newElement() {
        return new ManualFundTransfer("", period, getTrackingDatabase().getDefault(FundEvent.class), 0.0, getTrackingDatabase().getDefault(Currency.class));
    }
}
