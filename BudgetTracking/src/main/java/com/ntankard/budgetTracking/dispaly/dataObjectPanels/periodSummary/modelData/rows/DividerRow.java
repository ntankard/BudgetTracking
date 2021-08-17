package com.ntankard.budgetTracking.dispaly.dataObjectPanels.periodSummary.modelData.rows;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.dispaly.dataObjectPanels.periodSummary.modelData.ModelData_Columns;

public class DividerRow<P extends Pool> extends DataRows<P> {

    /**
     * The name to put on the divider
     */
    private String name;

    /**
     * @inheritDoc
     */
    public DividerRow(String name, Period core, ModelData_Columns<P> columns) {
        super(core, columns, Transfer.class);
        this.name = name;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getRowCount() {
        return 1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getTotal(Pool pool) {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public double getTotal_impl(Pool pool) {
        return 0.0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValue(Pool pool, Currency currency, int rowIndex) {
        return name;
    }
}
