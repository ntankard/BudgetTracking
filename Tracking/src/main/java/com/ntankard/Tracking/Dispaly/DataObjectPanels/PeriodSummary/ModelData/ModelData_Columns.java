package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.util.TwoParent_Children_Set;

import java.util.ArrayList;
import java.util.List;

public class ModelData_Columns<P extends Pool> {

    // Core data
    private final Class<P> columnClass;
    private final Period core;

    // Column data
    public final List<P> pools = new ArrayList<>();
    private final List<Column> columns = new ArrayList<>();

    /**
     * Constructor
     *
     * @param core The Period this table is built around
     */
    public ModelData_Columns(Period core, Class<P> columnClass) {
        this.core = core;
        this.columnClass = columnClass;
        update();
    }

    /**
     * Recalculate the column data
     */
    public void update() {
        pools.clear();
        columns.clear();

        // Find all categories
        pools.addAll(TrackingDatabase.get().get(columnClass));

        for (P pool : pools) {

            // Find all the currencies used in this category
            List<Currency> transferCurrencies = getCurrencies(pool);

            if (transferCurrencies.size() != 0) {
                // Add the name field TODO if all is blank you may want to remove this column
                columns.add(new Column(pool, null, 1 + transferCurrencies.size(), 0));

                // Add a column for each currency
                for (Currency currency : transferCurrencies) {
                    columns.add(new Column(pool, currency, 1 + transferCurrencies.size(), columns.get(columns.size() - 1).index + 1));
                }
            }
        }
    }

    /**
     * Get all currencies that transaction are in for a specific pool
     *
     * @param pool The pool to get
     * @return All currencies that transaction are in for a specific pool
     */
    private List<Currency> getCurrencies(P pool) {
        return getSumSet(pool).getCurrencies();
    }

    /**
     * Get the name of the column
     *
     * @param column The column to get
     * @return The name of the column
     */
    public String getColumnName(int column) {
        if (columns.get(column).index == 0) {
            return columns.get(column).pool.toString();
        } else {
            return columns.get(column).pool.toString();
        }
    }

    /**
     * Get the number of columns
     *
     * @return The number of columns
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Is this the end of a category section?
     *
     * @param columnIndex The column to check
     * @return True if this is the end of the category section
     */
    public boolean isEndOfSection(int columnIndex) {
        return columns.get(columnIndex).index == columns.get(columnIndex).count - 1;
    }

    /**
     * Is this the center column for a category
     *
     * @param columnIndex The column to check
     * @return True if this is the center column for a category
     */
    public boolean isCenter(int columnIndex) {
        return columns.get(columnIndex).index == 1;
    }

    /**
     * Is this the center of the table?
     *
     * @param columnIndex The column to check
     * @return True if this the center of the table
     */
    public boolean isCenterTable(int columnIndex) {
        return columnIndex == columns.size() / 2;
    }

    /**
     * Is this the description column for a category
     *
     * @param columnIndex The column to check
     * @return True if this is the description column for a category
     */
    public boolean isDescription(int columnIndex) {
        return columns.get(columnIndex).index == 0;
    }

    /**
     * Get the currency for this column
     *
     * @param columnIndex The column to get
     * @return The currency for this column
     */
    public Currency getCurrency(int columnIndex) {
        return columns.get(columnIndex).currency;
    }

    /**
     * Get the category for this column
     *
     * @param columnIndex The column to get
     * @return The category for this column
     */
    public P getCategory(int columnIndex) {
        return columns.get(columnIndex).pool;
    }

    /**
     * Column Enum
     */
    private class Column {
        Column(P pool, Currency currency, int count, int index) {
            this.pool = pool;
            this.currency = currency;
            this.count = count;
            this.index = index;
        }

        P pool;
        Currency currency;
        int count;
        int index;
    }


    private TwoParent_Children_Set<HalfTransfer, Period, Pool> getSet(P pool) {
        return new TwoParent_Children_Set<>(HalfTransfer.class, core, pool, new TransferDestination_HalfTransfer_Filter(pool.getClass()));
    }

    private PeriodPool_SumSet getSumSet(P pool) {
        return new PeriodPool_SumSet(getSet(pool));
    }
}
