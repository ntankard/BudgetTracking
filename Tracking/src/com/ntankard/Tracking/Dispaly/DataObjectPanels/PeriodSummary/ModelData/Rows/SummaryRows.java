package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

public class SummaryRows<P extends Pool> extends DataRows<P> {

    /**
     * {@inheritDoc
     */
    public SummaryRows(Period core, ModelData_Columns<P> columns) {
        super(core, columns, Transfer.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getRowCount() {
        return 2;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValue(P pool, Currency currency, int rowIndex) {
        return null;
    }
}
