package com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets.PeriodCategory_Set;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.ModelData_Columns;

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
        return new PeriodCategory_Set(core, category).getTotal();
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
