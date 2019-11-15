package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Interface.Set.PeriodPoolType_Set;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.List;

public class TransferRow<T extends Transfer> extends DataRows<T> {

    /**
     * The class type of T
     */
    private final Class<T> typeParameterClass;

    /**
     * Constructor
     *
     * @param core    The Period this table is built around
     * @param columns The columns of the table
     */
    public TransferRow(Period core, ModelData_Columns columns, Class<T> typeParameterClass) {
        super(core, columns);
        this.typeParameterClass = typeParameterClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal_impl(Category category) {
        return new PeriodPoolType_Set<>(core, category, typeParameterClass).getTotal();
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
                return rowData.getDescription();
            } else {
                if (getValueCurrency(rowData).equals(currency)) {
                    return currency.getNumberFormat().format(getValue(rowData, category));
                }
            }
        }

        return "";
    }

    /**
     * Get the core data object that is driving this cell (same return for multiple rows)
     *
     * @param category The category to search in
     * @param rowIndex The transaction number to get
     * @return The core object
     */
    public DataObject getDataObject(Category category, int rowIndex) {
        List<T> categoryRows = this.rows.get(category);

        if (rowIndex < categoryRows.size()) {
            return categoryRows.get(rowIndex);
        }

        return null;
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
            return currency.getNumberFormat().format(total);
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
        if (rowData.isThisSource(category)) {
            return -rowData.getValue();
        }
        if (rowData.isThisDestination(category)) {
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
        return new PeriodPoolType_Set<>(core, category, typeParameterClass).getTotal(currency);
    }

    /**
     * Extract all the rows for a specified category
     *
     * @param category The category to get
     * @return All the rows for a specified category
     */
    private List<T> getRows(Category category) {
        return new ArrayList<T>(new PeriodPoolType_Set<>(core, category, typeParameterClass).get());
    }

    /**
     * {@inheritDoc
     */
    private Currency getValueCurrency(T rowData) {
        return rowData.getCurrency();
    }
}
