package com.ntankard.Tracking.Frames.Swing;

import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transaction;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
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
    private Map<Category, List<CategoryTransfer>> categoryTransfers = new HashMap<>();
    private int transactionMax = 0;
    private int categoryTransferMax = 0;

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
     * @param value       The value for the row in the transaction section
     */
    private void getTransactionAt(int rowIndex, int columnIndex, RendererObject value) {
        if (rowIndex == 0) { // Total summary
            getTransactionTotalAt(columnIndex, value);
        } else if (rowIndex == 1) { // Currency summary
            getTransactionCurrencyTotalAt(columnIndex, value);
        } else { // Main data
            getTransactionDataAt(rowIndex - 2, columnIndex, value);
        }
    }

    /**
     * Get the value for the row in the total section
     *
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getTransactionTotalAt(int columnIndex, RendererObject value) {
        // Get the core data
        if (columnIndex != 0) {
            if (columnsDataCurrency.get(columnIndex - 1) == null) {
                value.coreObject = CURRENCY_FORMAT.get(trackingDatabase.getCurrency("YEN")).format(core.getTransactionSummaries().get(columnsCategories.get(columnIndex)).getTotal());
            }
        }

        // Format the display
        value.bottom = THICK_LINE;
    }

    /**
     * Get the value for the row in the currency summary section
     *
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getTransactionCurrencyTotalAt(int columnIndex, RendererObject value) {
        // Get the core data
        Currency toSum = columnsDataCurrency.get(columnIndex);
        if (toSum != null) {
            double total = core.getTransactionSummaries().get(columnsCategories.get(columnIndex)).getTotal(toSum);
            value.coreObject = CURRENCY_FORMAT.get(toSum).format(total);
        }

        // Format the display
        value.bottom = THICK_LINE;
        if (columnIndex < columnsCategories.size() - 1) {
            if (columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                value.right = STANDARD_LINE;
            }
        }
    }

    /**
     * Get the value for the row in the main data section of the transaction section
     *
     * @param rowIndex    The row shifted so 0 is the start ofd the transaction data section
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getTransactionDataAt(int rowIndex, int columnIndex, RendererObject value) {
        // Get the core data
        List<Transaction> trans = this.transactions.get(columnsCategories.get(columnIndex));
        if (rowIndex < trans.size()) {
            if (columnsDataCurrency.get(columnIndex) == null) {
                value.coreObject = trans.get(rowIndex).getDescription();
            } else {
                Currency currency = columnsDataCurrency.get(columnIndex);
                if (trans.get(rowIndex).getIdStatement().getIdBank().getCurrency().equals(currency)) {
                    value.coreObject = CURRENCY_FORMAT.get(currency).format(trans.get(rowIndex).getValue());
                }
            }
        }

        // Format the display
        value.bottom = STANDARD_LINE;
        if (rowIndex == transactionMax - 1) {
            value.bottom = THICK_LINE;
        }
        if (columnIndex < columnsCategories.size() - 1) {
            if (columnsDataCurrency.get(columnIndex) == null) {
                if (columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                    value.right = STANDARD_LINE;
                }
            }
        }
    }

    /**
     * Get the value for the row in the CategoryTransfer section
     *
     * @param rowIndex    The row shifted so 0 is the start ofd the CategoryTransfer section
     * @param columnIndex The column
     * @param value       The value for the row in the CategoryTransfer section
     */
    private void getCategoryTransferAt(int rowIndex, int columnIndex, RendererObject value) {
        if (rowIndex == 0) { // Total summary
            getCategoryTransferTotalAt(columnIndex, value);
        } else if (rowIndex == 1) { // Currency summary
            getCategoryTransferCurrencyTotalAt(columnIndex, value);
        } else { // Main data
            getCategoryTransferDataAt(rowIndex - 2, columnIndex, value);
        }
    }

    /**
     * Get the value for the row in the total section
     *
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getCategoryTransferTotalAt(int columnIndex, RendererObject value) {
        // Get the core data
        if (columnIndex != 0) {
            if (columnsDataCurrency.get(columnIndex - 1) == null) {
                value.coreObject = CURRENCY_FORMAT.get(trackingDatabase.getCurrency("YEN")).format(core.getCategoryTransferSummaries().get(columnsCategories.get(columnIndex)).getTotal());
            }
        }

        // Format the display
        value.bottom = THICK_LINE;
    }

    /**
     * Get the value for the row in the currency summary section
     *
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getCategoryTransferCurrencyTotalAt(int columnIndex, RendererObject value) {
        // Get the core data
        Currency toSum = columnsDataCurrency.get(columnIndex);
        if (toSum != null) {
            double total = core.getCategoryTransferSummaries().get(columnsCategories.get(columnIndex)).getTotal(toSum);
            value.coreObject = CURRENCY_FORMAT.get(toSum).format(total);
        }

        // Format the display
        value.bottom = THICK_LINE;
        if (columnIndex < columnsCategories.size() - 1) {
            if (columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                value.right = STANDARD_LINE;
            }
        }
    }

    /**
     * Get the value for the row in the main data section of the CategoryTransfer section
     *
     * @param rowIndex    The row shifted so 0 is the start ofd the CategoryTransfer data section
     * @param columnIndex The column
     * @param value       The value for the row with forming information
     */
    private void getCategoryTransferDataAt(int rowIndex, int columnIndex, RendererObject value) {
        // Get the core data
        List<CategoryTransfer> transfers = this.categoryTransfers.get(columnsCategories.get(columnIndex));
        if (rowIndex < transfers.size()) {
            if (columnsDataCurrency.get(columnIndex) == null) {
                value.coreObject = transfers.get(rowIndex).getDescription();
            } else {
                Currency currency = columnsDataCurrency.get(columnIndex);
                if (transfers.get(rowIndex).getCurrency().equals(currency)) {
                    value.coreObject = CURRENCY_FORMAT.get(currency).format(transfers.get(rowIndex).getValue());
                }
            }
        }


        // Format the display
        value.bottom = STANDARD_LINE;
        if (rowIndex == transactionMax - 1) {
            value.bottom = THICK_LINE;
        }
        if (columnIndex < columnsCategories.size() - 1) {
            if (columnsDataCurrency.get(columnIndex) == null) {
                if (columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                    value.right = STANDARD_LINE;
                }
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getRowCount() {
        return 1 + 2 + transactionMax + 2 + categoryTransferMax;
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
        RendererObject value = new RendererObject();

        // Thick lines for the column separator
        value.right = THICK_LINE;
        if (columnIndex < columnsCategories.size() - 1) {
            if (columnsCategories.get(columnIndex).equals(columnsCategories.get(columnIndex + 1))) {
                value.right = BLANK_LINE;
            }
        }

        if (rowIndex == 0) {  // Summary row
            // TBD
        } else if (rowIndex <= transactionMax + 2) { // Transaction rows
            getTransactionAt(rowIndex - 1, columnIndex, value);
        } else {
            getCategoryTransferAt(rowIndex - 1 - 2 - transactionMax, columnIndex, value);
        }

        return value;
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
        categoryTransfers.clear();
        categoryTransferMax = 0;

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

            // Populate categoryTransfers data
            List<CategoryTransfer> catCategoryTransfers = new ArrayList<>(core.getCategoryTransferSummaries().get(category).getEvents());
            if (catCategoryTransfers.size() > categoryTransferMax) {
                categoryTransferMax = catCategoryTransfers.size();
            }
            categoryTransfers.put(category, catCategoryTransfers);
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
