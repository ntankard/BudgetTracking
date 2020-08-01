package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

public class DividerRow<P extends Pool> extends DataRows<P> {

    /**
     * The name to put on the divider
     */
    private String name;

    /**
     * {@inheritDoc
     */
    public DividerRow(String name, Period core, ModelData_Columns<P> columns) {
        super(core, columns, Transfer.class);
        this.name = name;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getRowCount() {
        return 1;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getTotal(Pool pool) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Pool pool) {
        return 0.0;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValue(Pool pool, Currency currency, int rowIndex) {
        return name;
    }
}
