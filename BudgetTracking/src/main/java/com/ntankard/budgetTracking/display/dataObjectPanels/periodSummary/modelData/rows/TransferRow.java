package com.ntankard.budgetTracking.display.dataObjectPanels.periodSummary.modelData.rows;

import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.display.dataObjectPanels.periodSummary.modelData.ModelData_Columns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferRow<P extends Pool> extends DataRows<P> {

    /**
     * Store the row data to prevent recalculation
     */
    protected final Map<P, List<HalfTransfer>> rows = new HashMap<>();

    /**
     * Constructor
     *
     * @param core    The Period this table is built around
     * @param columns The columns of the table
     */
    public TransferRow(Period core, ModelData_Columns<P> columns, Class<? extends Transfer> typeParameterClass) {
        super(core, columns, typeParameterClass);
    }

    /**
     * Recalculate the row data
     */
    public void update() {
        rows.clear();
        maxRows = 0;

        for (P pool : columns.pools) {
            // Populate rows data
            List<HalfTransfer> rowData = getRows(pool);
            if (maxRows < rowData.size()) {
                maxRows = rowData.size();
            }
            rows.put(pool, rowData);
        }
    }

    /**
     * Get an individual transaction value
     *
     * @param pool     The pool to search in
     * @param currency The currency to get (0 if the currency dose not match)
     * @param rowIndex The transaction number to get
     * @return The formatted value
     */
    public Object getValue(P pool, Currency currency, int rowIndex) {
        List<HalfTransfer> categoryRows = this.rows.get(pool);

        if (rowIndex < categoryRows.size()) {
            HalfTransfer rowData = categoryRows.get(rowIndex);
            if (currency == null) {
                return rowData.getTransfer().getDescription();
            } else {
                if (getValueCurrency(rowData, pool).equals(currency)) {
                    return currency.getNumberFormat().format(getValue(rowData, pool));
                }
            }
        }

        return "";
    }

    /**
     * Get the core data object that is driving this cell (same return for multiple rows)
     *
     * @param pool     The pool to search in
     * @param rowIndex The transaction number to get
     * @return The core object
     */
    public DataObject getDataObject(P pool, int rowIndex) {
        List<HalfTransfer> categoryRows = this.rows.get(pool);

        if (rowIndex < categoryRows.size()) {
            return categoryRows.get(rowIndex);
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    public String getDescription(Transfer rowData) {
        return rowData.getDescription();
    }

    /**
     * Get the total for a specific currency used in a pool
     *
     * @param pool     The pool to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    public Object getCurrencyTotal(P pool, Currency currency) {
        if (currency != null) {
            double total = getCurrencyTotal_impl(pool, currency);
            return currency.getNumberFormat().format(total);
        }
        return null;
    }

    /**
     * Extract the value for this specific row type
     *
     * @param rowData The data for this row
     * @param pool    The category of the column
     * @return The Value
     */
    private double getValue(HalfTransfer rowData, P pool) {
        return rowData.getValue();
    }

    /**
     * Get the total for a specific currency used in a category for the specific row type
     *
     * @param pool     The category to get the total for
     * @param currency The currency to get the total for
     * @return The formatted total
     */
    private double getCurrencyTotal_impl(P pool, Currency currency) {
        return getSumSet(pool).getTotal(currency);
    }

    /**
     * Extract all the rows for a specified category
     *
     * @param pool The category to get
     * @return All the rows for a specified category
     */
    private List<HalfTransfer> getRows(P pool) {
        return new ArrayList<>(getSumSet(pool).get());
    }

    /**
     * @inheritDoc
     */
    private Currency getValueCurrency(HalfTransfer rowData, P pool) {
        return rowData.getCurrency();
    }
}
