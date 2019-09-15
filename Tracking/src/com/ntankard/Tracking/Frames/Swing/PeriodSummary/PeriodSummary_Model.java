package com.ntankard.Tracking.Frames.Swing.PeriodSummary;

import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Columns;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.ModelData_Rows;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows.DataRows;

import javax.swing.table.AbstractTableModel;

import static com.ntankard.Tracking.Frames.Swing.PeriodSummary.PeriodSummary_Renderer.RendererObject;

public class PeriodSummary_Model extends AbstractTableModel implements Updatable {

    // Line sizes
    private static final int BLANK_LINE = 0;
    private static final int STANDARD_LINE = 1;
    private static final int THICK_LINE = 3;

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

        if (sectionIndex < 2) { // Summary section
            if (sectionIndex == 0) { // Total
                if (columns.isCenter(columnIndex)) {
                    value.coreObject = dataRow.getTotal(category);
                }
            } else if (sectionIndex == 1) { // Currency total
                value.coreObject = dataRow.getCurrencyTotal(category, currency);

                // Add formatting
                value.right = STANDARD_LINE;
            }

            // Add formatting
            value.bottom = THICK_LINE;
        } else { // Core data section
            // Get the data
            value.coreObject = dataRow.getValue(category, currency, sectionIndex - 2);

            // Add formatting
            value.bottom = STANDARD_LINE;
            if (columns.isDescription(columnIndex)) {
                value.right = STANDARD_LINE;
            }
        }

        // Thick lines for the column separator
        if (columns.isEndOfSection(columnIndex)) {
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
