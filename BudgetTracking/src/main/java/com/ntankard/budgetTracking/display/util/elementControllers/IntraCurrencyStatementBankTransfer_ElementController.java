package com.ntankard.budgetTracking.display.util.elementControllers;

        import com.ntankard.budgetTracking.dataBase.core.Currency;
        import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder;
        import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
        import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
        import com.ntankard.budgetTracking.dataBase.core.transfer.bank.statement.IntraCurrencyStatementBankTransfer;
        import com.ntankard.budgetTracking.dataBase.core.transfer.bank.statement.StatementBankTransfer;
        import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
        import com.ntankard.dynamicGUI.gui.util.update.Updatable;
        import com.ntankard.javaObjectDatabase.exception.nonCorrupting.NonCorruptingException;
        import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;

        import java.util.List;

public class IntraCurrencyStatementBankTransfer_ElementController extends Database_ElementController<IntraCurrencyStatementBankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private final StatementFolder statementFolder;

    /**
     * Constructor
     */
    public IntraCurrencyStatementBankTransfer_ElementController(StatementFolder statementFolder, Updatable master) {
        super(statementFolder.getTrackingDatabase(), master);
        this.statementFolder = statementFolder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IntraCurrencyStatementBankTransfer newElement() {

        Bank otherBank = null;
        for(Bank bank: statementFolder.getTrackingDatabase().get(Bank.class)){
            if(bank.getCurrency() != statementFolder.getBank().getCurrency()){
                otherBank = bank;
                break;
            }
        }
        if(otherBank == null){
            throw new NonCorruptingException("There must be at-least 2 banks of 2 currencies");
        }

        return new IntraCurrencyStatementBankTransfer(statementFolder.getPeriod(), statementFolder.getBank(), null, otherBank, "", null, 1.0, 0.0);
    }
}
