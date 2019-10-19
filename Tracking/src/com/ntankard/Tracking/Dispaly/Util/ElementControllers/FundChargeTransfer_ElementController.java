package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;


public class FundChargeTransfer_ElementController implements DynamicGUI_DisplayList.ElementController<FundChargeTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * The container to notify if the buttons make a database change
     */
    private Updatable master;

    /**
     * Constructor
     */
    public FundChargeTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FundChargeTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextPeriodFundTransferId();
        return new FundChargeTransfer(
                idCode,
                period,
                TrackingDatabase.get().getFunds().get(0),
                TrackingDatabase.get().getCurrency("YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(FundChargeTransfer toDel) {
        TrackingDatabase.get().removeFundChargeTransfer(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(FundChargeTransfer newObj) {
        TrackingDatabase.get().addFundChargeTransfer(newObj);
        master.notifyUpdate();
    }
}
