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
    public List<Object> getRows(Category category) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return core.getPeriodTransferSummaries().get(category).getTotal() +
                core.getTransactionSummaries().get(category).getTotal() +
                core.getCategoryTransferSummaries().get(category).getTotal() +
                core.getNonPeriodFundTransferSummaries().get(category).getTotal();
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
    public double getCurrencyTotal_impl(Category category, Currency currency) {
        return 0;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValue(Category category, Currency currency, int rowIndex) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getDescription(Object rowData) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getValueCurrency(Object rowData) {
        return null;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getValue(Object rowData, Category category) {
        return 0.0;
    }
}
