package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.NonPeriodFundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;


public class NonPeriodChargeTransfer_ElementController implements DynamicGUI_DisplayList.ElementController<NonPeriodFundChargeTransfer> {

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
    public NonPeriodChargeTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public NonPeriodFundChargeTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextNonPeriodFundTransferId();
        return new NonPeriodFundChargeTransfer(
                idCode,
                period,
                TrackingDatabase.get().getNonPeriodFunds().get(0),
                TrackingDatabase.get().getNonPeriodFunds().get(0).<NonPeriodFundEvent>getChildren(NonPeriodFundEvent.class).get(0),
                TrackingDatabase.get().getCurrency("YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(NonPeriodFundChargeTransfer toDel) {
        TrackingDatabase.get().removeNonPeriodFundChargeTransfer(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(NonPeriodFundChargeTransfer newObj) {
        TrackingDatabase.get().addNonPeriodFundChargeTransfer(newObj);
        master.notifyUpdate();
    }
}
