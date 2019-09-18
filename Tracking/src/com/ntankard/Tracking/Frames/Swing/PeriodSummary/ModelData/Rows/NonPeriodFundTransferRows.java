package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class NonPeriodFundTransferRows extends DataRows<NonPeriodFundTransfer> {

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public NonPeriodFundTransferRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getTotal_impl(Category category) {
        return core.getNonPeriodFundTransferSummaries().get(category).getTotal();
    }

    @Override
    protected double getCurrencyTotal_impl(Category category, Currency currency) {
        return core.getNonPeriodFundTransferSummaries().get(category).getTotal(currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected String getDescription(NonPeriodFundTransfer rowData) {
        return rowData.getDescription();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Currency getValueCurrency(NonPeriodFundTransfer rowData) {
        return rowData.getCurrency();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getValue(NonPeriodFundTransfer rowData, Category category) {
        return -rowData.getValue();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<NonPeriodFundTransfer> getRows(Category category) {
        return new ArrayList<>(core.getNonPeriodFundTransferSummaries().get(category).getEvents());
    }
}
