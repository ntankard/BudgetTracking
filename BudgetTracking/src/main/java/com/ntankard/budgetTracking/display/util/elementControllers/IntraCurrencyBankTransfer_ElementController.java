package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.IntraCurrencyBankTransfer;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;

import java.util.List;

public class IntraCurrencyBankTransfer_ElementController extends Database_ElementController<IntraCurrencyBankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private final Period period;

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer_ElementController(Period period, Updatable master) {
        super(period.getTrackingDatabase(), master);
        this.period = period;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IntraCurrencyBankTransfer newElement() {
        List<Currency> currencies = getTrackingDatabase().get(Currency.class);

        Bank bank1 = new OneParent_Children_Set<>(Bank.class, currencies.get(0)).get().get(0);
        Bank bank2 = new OneParent_Children_Set<>(Bank.class, currencies.get(1)).get().get(0);

        return new IntraCurrencyBankTransfer("", period, bank1, 0.0, null, bank2, 0.0);
    }
}
