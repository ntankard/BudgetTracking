package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Columns;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.ModelData_Rows;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows.DataRows;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows.DividerRow;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.ModelData.Rows.TransferRow;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Renderer.RendererObject;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodSummary_Model<P extends Pool> extends AbstractTableModel implements Updatable {

    // External formatting
    public interface CustomFormatter {
        /**
         * Apply any special forming to the rendererObject based on the dataObject
         *
         * @param dataObject     The object to asses
         * @param rendererObject The object to store the custom forming
         */
        void doFormat(DataObject dataObject, RendererObject rendererObject);
    }

    private List<CustomFormatter> formatterList = new ArrayList<>();

    // Line sizes
    private static final int BLANK_LINE = 0;
    private static final int STANDARD_LINE = 1;
    private static final int THICK_LINE = 3;

    // Colors
    private final static Color HIGHLIGHTED_BACKGROUND = new Color(220, 220, 220);
    private final static Color HIGHLIGHTED_TEXT = Color.RED;

    // Table data
    private final ModelData_Columns<P> columns;
    private final ModelData_Rows<P> rows;

    /**
     * Constructor
     */
    public PeriodSummary_Model(Period core, boolean addTransfers, Class<P> columnClass) {
        this.columns = new ModelData_Columns<>(core, columnClass);
        this.columns.update();
        this.rows = new ModelData_Rows<>(core, columns, addTransfers);

        update();
    }

    /**
     * Add a new custom formatter for data cells
     *
     * @param customFormatter The formatter to add
     */
    public void addCustomFormatter(CustomFormatter customFormatter) {
        formatterList.add(customFormatter);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getRowCount() {
        return rows.getRowCount();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public int getColumnCount() {
        return columns.getColumnCount();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        return columns.getColumnName(column);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RendererObject value = new RendererObject();

        // Get information about the cell
        DataRows<P> dataRow = rows.getDataRow(rowIndex);
        int sectionIndex = rows.getSectionIndex(rowIndex);
        Currency currency = columns.getCurrency(columnIndex);
        P pool = columns.getCategory(columnIndex);

        if (dataRow instanceof DividerRow) {
            if (columns.isCenterTable(columnIndex)) {
                value.dataObject = dataRow.getValue(null, null, rowIndex);
            }
            value.background = HIGHLIGHTED_BACKGROUND;
        } else if (sectionIndex == 0) { // Category name

            if (columns.isCenter(columnIndex)) {
                value.dataObject = columns.getCategory(columnIndex).toString();
            }
            value.bottom = STANDARD_LINE;

        } else if (sectionIndex == 1) { // Total

            if (columns.isCenter(columnIndex)) {
                value.dataObject = dataRow.getTotal(pool);
                value.foreground = HIGHLIGHTED_TEXT;
                value.isBold = true;
                //value.foreground = new Color(222, 149, 47);

            }
            value.background = HIGHLIGHTED_BACKGROUND;
            value.bottom = STANDARD_LINE;

        } else if (sectionIndex == 2) { // Currency name

            if (columns.getCurrency(columnIndex) != null) {
                value.dataObject = columns.getCurrency(columnIndex).toString();
                value.bottom = STANDARD_LINE;
            }
            value.right = STANDARD_LINE;

        } else if (sectionIndex == 3) { // Currency total

            if (columns.getCurrency(columnIndex) != null) {
                value.dataObject = dataRow.getCurrencyTotal(pool, currency);
                value.foreground = HIGHLIGHTED_TEXT;
                value.background = HIGHLIGHTED_BACKGROUND;
                value.bottom = STANDARD_LINE;
                value.isBold = true;
            }
            value.right = STANDARD_LINE;

        } else if (sectionIndex == 4) {// Divider

            value.bottom = STANDARD_LINE;

        } else { // Data list
            if (dataRow instanceof TransferRow<?>) {
                TransferRow<P> transferRow = (TransferRow<P>) dataRow;
                DataObject obj = transferRow.getDataObject(pool, sectionIndex - 5);
                if (obj != null) {
                    for (CustomFormatter customFormatter : formatterList) {
                        customFormatter.doFormat(obj, value);
                    }
                }
            }

            value.dataObject = dataRow.getValue(pool, currency, sectionIndex - 5);

            value.bottom = STANDARD_LINE;
            if (columns.isDescription(columnIndex)) {
                value.right = STANDARD_LINE;
            }
        }

        // Thick lines for the column separator
        if (columns.isEndOfSection(columnIndex) && !(dataRow instanceof DividerRow)) {
            value.right = THICK_LINE;
        }
        if (rows.isEndOfSection(rowIndex)) {
            value.bottom = THICK_LINE;
        }
        return value;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        int count = getColumnCount();

        columns.update();
        rows.update();

        // Apply the update
        if (count != getColumnCount()) {
            fireTableStructureChanged();
        }
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyUpdate() {
    }
}
