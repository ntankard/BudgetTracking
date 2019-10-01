package com.ntankard.Tracking.Dispaly.Swing.PeriodSummary;

import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.ModelData_Columns;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.ModelData_Rows;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.Rows.DataRows;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.ModelData.Rows.DividerRow;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.PeriodSummary_Renderer.RendererObject;

import javax.swing.table.AbstractTableModel;
import java.awt.*;


public class PeriodSummary_Model extends AbstractTableModel implements Updatable {

    // Line sizes
    private static final int BLANK_LINE = 0;
    private static final int STANDARD_LINE = 1;
    private static final int THICK_LINE = 3;

    // Colors
    private final static Color HIGHLIGHTED_BACKGROUND = new Color(220, 220, 220);
    private final static Color HIGHLIGHTED_TEXT = Color.RED;

    // Core data
    private Period core;
    private TrackingDatabase trackingDatabase;

    // Table data
    private final ModelData_Columns columns;
    private final ModelData_Rows rows;

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     */
    public PeriodSummary_Model(TrackingDatabase trackingDatabase, Period core) {
        this.trackingDatabase = trackingDatabase;
        this.core = core;

        this.columns = new ModelData_Columns(trackingDatabase, core);
        this.columns.update();
        this.rows = new ModelData_Rows(trackingDatabase, core, columns);

        update();
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
        DataRows dataRow = rows.getDataRow(rowIndex);
        int sectionIndex = rows.getSectionIndex(rowIndex);
        Currency currency = columns.getCurrency(columnIndex);
        Category category = columns.getCategory(columnIndex);

        if (dataRow instanceof DividerRow) {
            if (columns.isCenterTable(columnIndex)) {
                value.coreObject = dataRow.getValue(null, null, rowIndex);
            }
            value.background = HIGHLIGHTED_BACKGROUND;
        } else if (sectionIndex == 0) { // Category name

            if (columns.isCenter(columnIndex)) {
                value.coreObject = columns.getCategory(columnIndex).getId();
            }
            value.bottom = STANDARD_LINE;

        } else if (sectionIndex == 1) { // Total

            if (columns.isCenter(columnIndex)) {
                value.coreObject = dataRow.getTotal(category);
                value.foreground = HIGHLIGHTED_TEXT;
                value.isBold = true;
                //value.foreground = new Color(222, 149, 47);

            }
            value.background = HIGHLIGHTED_BACKGROUND;
            value.bottom = STANDARD_LINE;

        } else if (sectionIndex == 2) { // Currency name

            if (columns.getCurrency(columnIndex) != null) {
                value.coreObject = columns.getCurrency(columnIndex).getId();
                value.bottom = STANDARD_LINE;
            }
            value.right = STANDARD_LINE;

        } else if (sectionIndex == 3) { // Currency total

            if (columns.getCurrency(columnIndex) != null) {
                value.coreObject = dataRow.getCurrencyTotal(category, currency);
                value.foreground = HIGHLIGHTED_TEXT;
                value.background = HIGHLIGHTED_BACKGROUND;
                value.bottom = STANDARD_LINE;
                value.isBold = true;
            }
            value.right = STANDARD_LINE;

        } else if (sectionIndex == 4) {// Divider

            value.bottom = STANDARD_LINE;

        } else { // Data list

            Object data = dataRow.getValue(category, currency, sectionIndex - 5);
            if (data instanceof MoneyEvent) {
                value.coreObject = ((MoneyEvent) data).getDescription();
            } else {
                value.coreObject = data;
            }

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
        columns.update();
        rows.update();

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
