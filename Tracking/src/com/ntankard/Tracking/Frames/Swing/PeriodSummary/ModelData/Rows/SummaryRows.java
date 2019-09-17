package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.List;

public class SummaryRows extends DataRows<Object> {

    /**
     * {@inheritDoc
     */
    public SummaryRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public void update() {

    }

    @Override
    public List<Object> getRows(Category category) {
        return null;
    }

    @Override
    public Object getTotal(Category category) {
        return null;
    }

    @Override
    public double getTotal_impl(Category category) {
        return 0.0;
    }

    @Override
    public Object getCurrencyTotal(Category category, Currency currency) {
        return null;
    }

    @Override
    public double getCurrencyTotal_impl(Category category, Currency currency) {
        return 0;
    }

    @Override
    public Object getValue(Category category, Currency currency, int rowIndex) {
        return null;
    }

    @Override
    public String getDescription(Object rowData) {
        return null;
    }

    @Override
    public Currency getValueCurrency(Object rowData) {
        return null;
    }

    @Override
    public double getValue(Object rowData, Category category) {
        return 0.0;
    }
}
