package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.tracking.dataBase.core.transfer.bank.ManualBankTransfer;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

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
        super(core.getTrackingDatabase(), master);
        this.core = core;
        this.bank = bank;
    }

    /**
     * Constructor
     */
    public ManualBankTransfer_ElementController(Period core, Updatable master) {
        super(core.getTrackingDatabase(), master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public BankTransfer newElement() {
        return new ManualBankTransfer("",
                core, bank, 0.0,
                null, getTrackingDatabase().getDefault(SolidCategory.class));
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
