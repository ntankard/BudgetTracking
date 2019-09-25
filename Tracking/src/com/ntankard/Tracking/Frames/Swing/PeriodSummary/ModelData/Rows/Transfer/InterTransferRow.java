package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows.Transfer;

import com.ntankard.Tracking.DataBase.Core.Base.Transfer;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class InterTransferRow<T extends Transfer> extends TransferRow<T> {

    /**
     * The class type of T
     */
    private final Class<T> typeParameterClass;

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public InterTransferRow(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns, Class<T> typeParameterClass) {
        super(trackingDatabase, core, columns);
        this.typeParameterClass = typeParameterClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getValue(T rowData, Category category) {
        if (rowData.isThisSource(core, category)) {
            return -rowData.getValue();
        }
        if (rowData.isThisDestination(core, category)) {
            return rowData.getValue();
        }
        throw new RuntimeException("Bad row");
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected double getCurrencyTotal_impl(Category category, Currency currency) {
        return core.getTransferSummary(typeParameterClass, category).getTotal(currency);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return core.getTransferSummary(typeParameterClass, category).getTotal();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<T> getRows(Category category) {
        if (core.getTransferSummary(typeParameterClass, category) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<T>(core.getTransferSummary(typeParameterClass, category).getEvents());
    }
}
