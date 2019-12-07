package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;
import com.ntankard.Tracking.Dispaly.Util.Comparators.Ordered_Comparator;

import java.util.ArrayList;
import java.util.List;

public class ModelData_Columns {

    // Core data
    private Period core;

    // Column data
    public final List<Category> categories = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();

    /**
     * Constructor
     *
     * @param core The Period this table is built around
     */
    public ModelData_Columns(Period core) {
        this.core = core;
        update();
    }

    /**
     * Recalculate the column data
     */
    public void update() {
        categories.clear();
        columns.clear();

        // Find all categories
        categories.addAll(TrackingDatabase.get().get(Category.class));
        categories.sort(new Ordered_Comparator());

        for (Category category : categories) {

            // Find all the currencies used in this category
            List<Currency> transferCurrencies = getCurrencies(category);

            if (transferCurrencies.size() != 0) {
                // Add the name field TODO if all is blank you may want to remove this column
                columns.add(new Column(category, null, 1 + transferCurrencies.size(), 0));

                // Add a column for each currency
                for (Currency currency : transferCurrencies) {
                    columns.add(new Column(category, currency, 1 + transferCurrencies.size(), columns.get(columns.size() - 1).index + 1));
                }
            }
        }
    }

    /**
     * Get all currencies that transaction are in for a specific category
     *
     * @param category The category to get
     * @return All currencies that transaction are in for a specific category
     */
    private List<Currency> getCurrencies(Category category) {
        return new TransferSet_Summary<>(Transfer.class, core, category).getCurrencies();
    }

    /**
     * Get the name of the column
     *
     * @param column The column to get
     * @return The name of the column
     */
    public String getColumnName(int column) {
        if (columns.get(column).index == 0) {
            return columns.get(column).category.toString();
        } else {
            return columns.get(column).category.toString();
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
    public Category getCategory(int columnIndex) {
        return columns.get(columnIndex).category;
    }

    /**
     * Column Enum
     */
    private class Column {
        Column(Category category, Currency currency, int count, int index) {
            this.category = category;
            this.currency = currency;
            this.count = count;
            this.index = index;
        }

        Category category;
        Currency currency;
        int count;
        int index;
    }
}
