package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.ManualBankTransfer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class ManualBankTransfer_ElementController extends TrackingDatabase_ElementController<BankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;
    private Bank bank;

    /**
     * Constructor
     */
    public ManualBankTransfer_ElementController(Period core, Bank bank, Updatable master) {
        super(master);
        this.core = core;
        this.bank = bank;
    }

    /**
     * Constructor
     */
    public ManualBankTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public BankTransfer newElement() {
        return ManualBankTransfer.make(TrackingDatabase.get().getNextId(), "",
                core, bank, 0.0,
                null, TrackingDatabase.get().getDefault(SolidCategory.class), null);
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param bank The data to use when creating a new object
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
