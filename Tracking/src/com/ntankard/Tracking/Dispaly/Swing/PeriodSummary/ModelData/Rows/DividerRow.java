package com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.ModelData_Columns;

public class DividerRow extends DataRows<Object> {

    /**
     * The name to put on the divider
     */
    private String name;

    /**
     * {@inheritDoc
     */
    public DividerRow(String name, TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
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
    public void update() {
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getTotal(Category category) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return 0.0;
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
        return name;
    }
}