package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class TransactionRows extends DataRows<Transaction> {

    /**
     * {@inheritDoc
     */
    public TransactionRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return core.getTransactionSummaries().get(category).getTotal();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getCurrencyTotal_impl(Category category, Currency currency) {
        return core.getTransactionSummaries().get(category).getTotal(currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getDescription(Transaction rowData) {
        return rowData.getDescription();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getValueCurrency(Transaction rowData) {
        return rowData.getIdStatement().getIdBank().getCurrency();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getValue(Transaction rowData, Category category) {
        return rowData.getValue();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Transaction> getRows(Category category) {
        return new ArrayList<>(core.getTransactionSummaries().get(category).getEvents());
    }
}
