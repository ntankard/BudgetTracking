package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

public class SummaryRows extends DataRows<Object> {

    /**
     * {@inheritDoc
     */
    public SummaryRows(Period core, ModelData_Columns columns) {
        super(core, columns);
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
    public void update() {

    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return new PeriodPool_SumSet(core, category).getTotal();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getCurrencyTotal(Category category, Currency currency) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValue(Category category, Currency currency, int rowIndex) {
        return null;
    }
}
