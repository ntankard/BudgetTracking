package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.transfer.FundFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.ManualFundTransfer;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

import java.util.List;

public class FundFundTransfer_ElementController extends TrackingDatabase_ElementController<FundFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private final Period period;

    /**
     * Constructor
     */
    public FundFundTransfer_ElementController(Period period, Updatable master) {
        super(period.getTrackingDatabase(), master);
        this.period = period;
    }

    /**
     * @inheritDoc
     */
    @Override
    public FundFundTransfer newElement() {
        List<FundEvent> funds = getTrackingDatabase().get(FundEvent.class);
        funds.remove(getTrackingDatabase().getDefault(FundEvent.class));

        return new FundFundTransfer("", period, getTrackingDatabase().getDefault(FundEvent.class), 0.0, getTrackingDatabase().getDefault(Currency.class),funds.get(0));
    }
}
