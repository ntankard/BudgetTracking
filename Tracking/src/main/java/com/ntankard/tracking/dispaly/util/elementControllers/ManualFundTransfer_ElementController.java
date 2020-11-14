package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.transfer.fund.ManualFundTransfer;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class ManualFundTransfer_ElementController extends TrackingDatabase_ElementController<ManualFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public ManualFundTransfer_ElementController(Period period, Updatable master) {
        super(period.getTrackingDatabase(), master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public ManualFundTransfer newElement() {
        return ManualFundTransfer.make(getTrackingDatabase().getNextId(), "",
                period, getTrackingDatabase().getDefault(FundEvent.class), 0.0, getTrackingDatabase().getDefault(Currency.class));
    }
}
