package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;

public class StatementTransactionAutoGroup_ElementController extends Database_ElementController<StatementTransactionAutoGroup> {

    /**
     * Data to use when creating a new object
     */
    private Bank bank;

    /**
     * Constructor
     */
    public StatementTransactionAutoGroup_ElementController(Database database, Updatable master) {
        super(database, master);
        bank = getTrackingDatabase().getDefault(Bank.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    public StatementTransactionAutoGroup newElement() {
        return new StatementTransactionAutoGroup(bank, "", "", bank.getTrackingDatabase().getDefault(Pool.class));
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
