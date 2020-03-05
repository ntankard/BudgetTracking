package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

public abstract class DataRows<P extends Pool> {

    // Core data
    protected final Period core;
    protected final ModelData_Columns<P> columns;
    protected final Class<? extends Transfer> typeParameterClass;

    // Row data
    protected int maxRows = 0;

    /**
     * Constructor
     *
     * @param core    The Period this table is built around
     * @param columns The columns of the table
     */
    public DataRows(Period core, ModelData_Columns<P> columns, Class<? extends Transfer> typeParameterClass) {
        this.core = core;
        this.columns = columns;
        this.typeParameterClass = typeParameterClass;
    }

    /**
     * Get total in the primary currency
     *
     * @param pool The pool to get the total for
     * @return The formatted total
     */
    public Object getTotal(P pool) {
        return TrackingDatabase.get().getDefault(Currency.class).getNumberFormat().format(getTotal_impl(pool));
    }

    /**
     * Get the total for a specific currency used in a pool
     *
     * @param pool     The pool to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    public Object getCurrencyTotal(P pool, Currency currency) {
        return null;
    }

    /**
     * Get an individual transaction value
     *
     * @param pool     The pool to search in
     * @param currency The currency to get (0 if the currency dose not match)
     * @param rowIndex The transaction number to get
     * @return The formatted value
     */
    public abstract Object getValue(P pool, Currency currency, int rowIndex);

    /**
     * Get the number of rows need for this section
     *
     * @return The number of rows need for this section
     */
    public int getRowCount() {
        return maxRows + 5;
    }

    /**
     * Recalculate the row data
     */
    public void update() {
    }

    /**
     * Get total in the primary currency for the specific row type
     *
     * @param pool The pool to get the total for
     * @return The formatted total
     */
    protected double getTotal_impl(P pool) {
        return getSumSet(pool).getTotal();
    }

    /**
     * Get the set for this Row
     *
     * @param pool The pool to get
     * @return The set
     */
    protected PeriodPool_SumSet getSumSet(P pool) {
        return new PeriodPool_SumSet(new TwoParent_Children_Set<>(HalfTransfer.class, core, pool, new TransferType_HalfTransfer_Filter(typeParameterClass, new TransferDestination_HalfTransfer_Filter(pool.getClass()))), pool);
    }
}
