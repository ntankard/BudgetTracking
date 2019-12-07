package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataRows<T> {

    // Core data
    protected final Period core;
    protected final ModelData_Columns columns;

    // Row data
    protected final Map<Category, List<T>> rows = new HashMap<>();
    protected int maxRows = 0;

    /**
     * Constructor
     *
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public DataRows(Period core, ModelData_Columns columns) {
        this.core = core;
        this.columns = columns;
    }

    /**
     * Get total in the primary currency
     *
     * @param category The category to get the total for
     * @return The formatted total
     */
    public Object getTotal(Category category) {
        return TrackingDatabase.get().getDefault(Currency.class).getNumberFormat().format(getTotal_impl(category));
    }

    /**
     * Get the total for a specific currency used in a category
     *
     * @param category The category to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    public abstract Object getCurrencyTotal(Category category, Currency currency);

    /**
     * Get an individual transaction value
     *
     * @param category The category to search in
     * @param currency The currency to get (0 if the currency dose not match)
     * @param rowIndex The transaction number to get
     * @return The formatted value
     */
    public abstract Object getValue(Category category, Currency currency, int rowIndex);

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
    public abstract void update();

    /**
     * Get total in the primary currency for the specific row type
     *
     * @param category The category to get the total for
     * @return The formatted total
     */
    protected abstract double getTotal_impl(Category category);
}
