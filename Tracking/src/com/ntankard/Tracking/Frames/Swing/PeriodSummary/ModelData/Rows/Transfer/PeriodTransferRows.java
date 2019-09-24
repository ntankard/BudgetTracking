package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows.Transfer;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.PeriodTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class PeriodTransferRows extends TransferRow<PeriodTransfer> {

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public PeriodTransferRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        super(trackingDatabase, core, columns);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getTotal_impl(Category category) {
        return core.getPeriodTransferSummaries().get(category).getTotal();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getCurrencyTotal_impl(Category category, Currency currency) {
        return core.getPeriodTransferSummaries().get(category).getTotal(currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getValue(PeriodTransfer rowData, Category category) {
        if (rowData.getSource().equals(core)) {
            return -rowData.getValue();
        }
        return rowData.getValue();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<PeriodTransfer> getRows(Category category) {
        return new ArrayList<>(core.getPeriodTransferSummaries().get(category).getEvents());
    }
}
