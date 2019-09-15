package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class CategoryTransferRows extends DataRows<CategoryTransfer> {

    /**
     * {@inheritDoc
     */
    public CategoryTransferRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return core.getCategoryTransferSummaries().get(category).getTotal();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getCurrencyTotal_impl(Category category, Currency currency) {
        return core.getCategoryTransferSummaries().get(category).getTotal(currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getDescription(CategoryTransfer rowData) {
        return rowData.getDescription();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getValueCurrency(CategoryTransfer rowData) {
        return rowData.getCurrency();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getValue(CategoryTransfer rowData) {
        return rowData.getValue();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<CategoryTransfer> getRows(Category category) {
        return new ArrayList<>(core.getCategoryTransferSummaries().get(category).getEvents());
    }
}
