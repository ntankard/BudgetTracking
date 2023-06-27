package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.statement.StatementBankTransfer;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;

public class StatementBankTransfer_ElementController extends Database_ElementController<StatementBankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private final StatementFolder statementFolder;

    /**
     * Constructor
     */
    public StatementBankTransfer_ElementController(StatementFolder statementFolder, Updatable master) {
        super(statementFolder.getTrackingDatabase(), master);
        this.statementFolder = statementFolder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public StatementBankTransfer newElement() {
        return new StatementBankTransfer(statementFolder.getPeriod(), statementFolder.getBank(), null, getTrackingDatabase().getDefault(SolidCategory.class), "", null, 1.0);
    }
}
