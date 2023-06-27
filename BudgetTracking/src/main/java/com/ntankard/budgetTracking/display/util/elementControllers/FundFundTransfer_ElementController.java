package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.FundFundTransfer;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;

import java.util.List;

public class FundFundTransfer_ElementController extends Database_ElementController<FundFundTransfer> {

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

        return new FundFundTransfer("", period, getTrackingDatabase().getDefault(FundEvent.class), 0.0, getTrackingDatabase().getDefault(Currency.class), funds.get(0));
    }
}
