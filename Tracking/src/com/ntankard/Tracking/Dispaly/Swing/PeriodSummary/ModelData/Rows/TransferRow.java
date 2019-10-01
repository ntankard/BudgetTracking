package com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets.PeriodCategoryType_Set;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class TransferRow<T extends MoneyEvent> extends DataRows<T> {

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
    public TransferRow(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns, Class<T> typeParameterClass) {
        super(trackingDatabase, core, columns);
        this.typeParameterClass = typeParameterClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return new PeriodCategoryType_Set(core, category, typeParameterClass).getTotal();
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
                return rowData;
            } else {
                if (getValueCurrency(rowData).equals(currency)) {
                    return trackingDatabase.getCurrencyFormat(currency).format(getValue(rowData, category));
                }
            }
        }

        return "";
    }

    /**
     * {@inheritDoc
     */
    public String getDescription(T rowData) {
        return rowData.getDescription();
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
     * Extract the value for this specific row type
     *
     * @param rowData  The data for this row
     * @param category The category of the column
     * @return The Value
     */
    private double getValue(T rowData, Category category) {
        if (rowData.isThisSource(core, category)) {
            return -rowData.getValue();
        }
        if (rowData.isThisDestination(core, category)) {
            return rowData.getValue();
        }
        throw new RuntimeException("Bad row");
    }

    /**
     * Get the total for a specific currency used in a category for the specific row type
     *
     * @param category The category to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    private double getCurrencyTotal_impl(Category category, Currency currency) {
        return new PeriodCategoryType_Set(core, category, typeParameterClass).getTotal(currency);
    }

    /**
     * Extract all the rows for a specified category
     *
     * @param category The category to get
     * @return All the rows for a specified category
     */
    private List<T> getRows(Category category) {
        return new ArrayList<T>(new PeriodCategoryType_Set(core, category, typeParameterClass).getMoneyEvents());
    }

    /**
     * {@inheritDoc
     */
    private Currency getValueCurrency(T rowData) {
        return rowData.getCurrency();
    }
}
