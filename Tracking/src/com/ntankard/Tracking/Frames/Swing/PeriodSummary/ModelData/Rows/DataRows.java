package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataRows<T> {

    // Core data
    protected final TrackingDatabase trackingDatabase;
    protected final Period core;
    protected final ModelData_Columns columns;

    // Row data
    protected final Map<Category, List<T>> rows = new HashMap<>();
    protected int maxRows = 0;

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public DataRows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        this.trackingDatabase = trackingDatabase;
        this.core = core;
        this.columns = columns;
        update();
    }

    /**
     * Get total in the primary currency
     *
     * @param category The category to get the total for
     * @return The formatted total
     */
    public Object getTotal(Category category) {
        return trackingDatabase.getCurrencyFormat("YEN").format(getTotal_impl(category));
    }

    /**
     * Get the total for a specific currency used in a category
     *
     * @param category The category to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    public Object getCurrencyTotal(Category category, Currency currency) {
        if (currency != null) {
            double total = getCurrencyTotal_impl(category, currency);
            return trackingDatabase.getCurrencyFormat(currency).format(total);
        }
        return null;
    }

    /**
     * Get an individual transaction value
     *
     * @param category The category to search in
     * @param currency The currency to get (0 if the currency dose not match)
     * @param rowIndex The transaction number to get
     * @return The formatted value
     */
    public Object getValue(Category category, Currency currency, int rowIndex) {
        List<T> categoryRows = this.rows.get(category);

        if (rowIndex < categoryRows.size()) {
            T rowData = categoryRows.get(rowIndex);
            if (currency == null) {
                return getDescription(rowData);
            } else {
                if (getValueCurrency(rowData).equals(currency)) {
                    return trackingDatabase.getCurrencyFormat(currency).format(getValue(rowData));
                }
            }
        }

        return "";
    }

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
        rows.clear();
        maxRows = 0;

        for (Category category : columns.categories) {
            // Populate rows data
            List<T> rowData = getRows(category);
            if (maxRows < rowData.size()) {
                maxRows = rowData.size();
            }
            rows.put(category, rowData);
        }
    }

    /**
     * Get total in the primary currency for the specific row type
     *
     * @param category The category to get the total for
     * @return The formatted total
     */
    protected abstract double getTotal_impl(Category category);

    /**
     * Get the total for a specific currency used in a category for the specific row type
     *
     * @param category The category to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    protected abstract double getCurrencyTotal_impl(Category category, Currency currency);

    /**
     * Extract the description for this specific row type
     *
     * @param rowData The data for this row
     * @return The description
     */
    protected abstract String getDescription(T rowData);

    /**
     * Extract the currency the value is in for this specific row type
     *
     * @param rowData The data for this row
     * @return The currency the value is in
     */
    protected abstract Currency getValueCurrency(T rowData);

    /**
     * Extract the value for this specific row type
     *
     * @param rowData The data for this row
     * @return The Value
     */
    protected abstract double getValue(T rowData);

    /**
     * Extract all the rows for a specified category
     *
     * @param category The category to get
     * @return All the rows for a specified category
     */
    protected abstract List<T> getRows(Category category);
}
