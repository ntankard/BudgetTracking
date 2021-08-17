package com.ntankard.budgetTracking.dispaly.dataObjectPanels.periodSummary.modelData.rows;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.dispaly.dataObjectPanels.periodSummary.modelData.ModelData_Columns;

public class SummaryRows<P extends Pool> extends DataRows<P> {

    /**
     * @inheritDoc
     */
    public SummaryRows(Period core, ModelData_Columns<P> columns) {
        super(core, columns, Transfer.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getRowCount() {
        return 2;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValue(P pool, Currency currency, int rowIndex) {
        return null;
    }
}
