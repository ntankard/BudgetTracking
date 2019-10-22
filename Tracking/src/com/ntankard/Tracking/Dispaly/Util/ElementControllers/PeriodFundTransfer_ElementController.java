package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

public class PeriodFundTransfer_ElementController implements ElementController<PeriodFundTransfer> {

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
    public PeriodFundTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public PeriodFundTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextId(PeriodFundTransfer.class);
        return new PeriodFundTransfer(
                idCode,
                period,
                TrackingDatabase.get().get(Fund.class).get(0),
                TrackingDatabase.get().get(Category.class,"Unaccounted"),
                TrackingDatabase.get().get(Fund.class).get(0).<FundEvent>getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(PeriodFundTransfer toDel) {
        TrackingDatabase.get().remove(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(PeriodFundTransfer newObj) {
        TrackingDatabase.get().add(newObj);
        master.notifyUpdate();
    }
}
