package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.CurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CurrencyBankTransfer_ElementController extends TrackingDatabase_ElementController<CurrencyBankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;

    /**
     * Constructor
     */
    public CurrencyBankTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CurrencyBankTransfer newElement() {
        return new CurrencyBankTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                core,
                TrackingDatabase.get().get(Bank.class).get(0),
                TrackingDatabase.get().get(Bank.class).get(1));
    }
}
