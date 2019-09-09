package com.ntankard.Tracking.Frames.Swing;

import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.*;

import static com.ntankard.Tracking.Frames.Swing.PeriodSummary_Renderer.*;

public class PeriodSummary_Model extends AbstractTableModel implements Updatable {

    // Line sizes
    private static final int BLANK_LINE = 0;
    private static final int STANDARD_LINE = 1;
    private static final int THICK_LINE = 4;

    private static final Map<Currency, NumberFormat> CURRENCY_FORMAT = new HashMap<>();

    // Core data
    private Period core;
    private TrackingDatabase trackingDatabase;

    // Column data
    private List<Category> columnsCategories = new ArrayList<>();
    private List<Currency> columnsDataCurrency = new ArrayList<>();

    // Row data
    private Map<Category, List<Transaction>> transactions = new HashMap<>();
    private int transactionMax = 0;

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     */
    public PeriodSummary_Model(TrackingDatabase trackingDatabase, Period core) {
        this.trackingDatabase = trackingDatabase;
        this.core = core;

        CURRENCY_FORMAT.put(trackingDatabase.getCurrency("AUD"), NumberFormat.getCurrencyInstance(Locale.US));
        CURRENCY_FORMAT.put(trackingDatabase.getCurrency("YEN"), NumberFormat.getCurrencyInstance(Locale.JAPAN));

        update();
    }

    /**
     * Get the value for the row in the transaction section
     *
     * @param rowIndex    The row shifted so 0 is the start ofd the transaction section
     * @param columnIndex The column
     * @return The value for the row in the transaction section
     */
    private Object getTransactionAt(int rowIndex, int columnIndex) {
        if (rowIndex < 2) { // Summary section
            int right = BLANK_LINE;
            if (columnIndex < columnsCategories.size() - 1) { // All except bottom
                if (!columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                    right = THICK_LINE;
                }
            } else { // Main data section
                right = THICK_LINE;
            }

            return new RendererObject("", THICK_LINE, right);
        } else {
            return getTransactionDataAt(rowIndex - 2, columnIndex);
        }
    }

    /**
     * Get the value for the row in the main data section of the transaction section
     *
     * @param rowIndex    The row shifted so 0 is the start ofd the transaction data section
     * @param columnIndex The column
     * @return The value for the row in the main data section of the transaction section
     */
    private Object getTransactionDataAt(int rowIndex, int columnIndex) {
        Object toReturn = "";

        // Get the core data
        List<Transaction> transactions = this.transactions.get(columnsCategories.get(columnIndex));
        if (rowIndex < transactions.size()) {
            if (columnsDataCurrency.get(columnIndex) == null) {
                toReturn = transactions.get(rowIndex).getDescription();
            } else {
                Currency currency = columnsDataCurrency.get(columnIndex);
                if (transactions.get(rowIndex).getIdStatement().getIdBank().getCurrency().equals(currency)) {
                    toReturn = CURRENCY_FORMAT.get(currency).format(transactions.get(rowIndex).getValue());
                }
            }
        }

        // Draw the borders
        int right = BLANK_LINE;
        if (columnIndex < columnsCategories.size() - 1) {
            if (!columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                right = THICK_LINE;
            }
            if (columnsDataCurrency.get(columnIndex) == null) {
                right = STANDARD_LINE;
            }
        } else {
            right = THICK_LINE;
        }

        return new RendererObject(toReturn, STANDARD_LINE, right);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getRowCount() {
        return 1 + transactionMax + 2;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getColumnCount() {
        return columnsCategories.size();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        if (columnsDataCurrency.get(column) == null) {
            return columnsCategories.get(column).getId();
        } else {
            return columnsDataCurrency.get(column).getId();
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) {
            // Summary row
            return "";
        } else {
            // Transaction rows
            return getTransactionAt(rowIndex - 1, columnIndex);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        columnsCategories.clear();
        columnsDataCurrency.clear();

        transactions.clear();
        transactionMax = 0;

        // Find all categories
        List<Category> categories = new ArrayList<>(trackingDatabase.getCategories());
        categories.sort(Comparator.comparingInt(Category::getOrder));

        for (Category category : categories) {

            // Add the name field TODO if all is blank you may want to remove this column
            columnsCategories.add(category);
            columnsDataCurrency.add(null);

            // Find all the currencies used in this category
            List<Currency> transactionCurrencies = core.getTransactionSummaries().get(category).getCurrencies();
            List<Currency> transferCurrencies = core.getCategoryTransferSummaries().get(category).getCurrencies();
            for (Currency currency : transactionCurrencies) {
                if (!transferCurrencies.contains(currency)) {
                    transferCurrencies.add(currency);
                }
            }
            transferCurrencies.sort(Comparator.comparing(Currency::getId).reversed());

            // Add a column for each currency
            for (Currency currency : transferCurrencies) {
                columnsCategories.add(category);
                columnsDataCurrency.add(currency);
            }

            // Populate transaction data
            List<Transaction> catTransactions = new ArrayList<>(core.getTransactionSummaries().get(category).getEvents());
            if (catTransactions.size() > transactionMax) {
                transactionMax = catTransactions.size();
            }
            transactions.put(category, catTransactions);
        }

        // Apply the update
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyUpdate() {
    }
}
