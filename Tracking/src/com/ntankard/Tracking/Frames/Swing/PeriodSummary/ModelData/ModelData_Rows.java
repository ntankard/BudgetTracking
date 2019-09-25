package com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.PeriodTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows.*;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.ModelData.Rows.Transfer.*;

import java.util.ArrayList;
import java.util.List;

public class ModelData_Rows {

    // Core data
    private Period core;
    private TrackingDatabase trackingDatabase;

    // Column data
    private List<Section> sections = new ArrayList<>();

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this table is built around
     * @param columns          The columns of the table
     */
    public ModelData_Rows(TrackingDatabase trackingDatabase, Period core, ModelData_Columns columns) {
        this.trackingDatabase = trackingDatabase;
        this.core = core;

        addSection(new SummaryRows(trackingDatabase, core, columns));
        addSection(new DividerRow("Transaction", trackingDatabase, core, columns));
        addSection(new TransactionRows(trackingDatabase, core, columns));
        addSection(new DividerRow("Category", trackingDatabase, core, columns));
        addSection(new InterTransferRow<>(trackingDatabase, core, columns, CategoryTransfer.class));
        addSection(new DividerRow("Period", trackingDatabase, core, columns));
        addSection(new InterTransferRow<>(trackingDatabase, core, columns, PeriodTransfer.class));
        addSection(new DividerRow("External", trackingDatabase, core, columns));
        addSection(new InterTransferRow<>(trackingDatabase, core, columns, NonPeriodFundTransfer.class));
    }

    /**
     * Add a new section to the table
     *
     * @param dataRows The rows to add
     */
    private void addSection(DataRows dataRows) {
        dataRows.update();
        int startIndex = 0;
        if (sections.size() != 0) {
            startIndex = sections.get(sections.size() - 1).endIndex + 1;
        }
        int endIndex = startIndex + dataRows.getRowCount() - 1;
        this.sections.add(new Section(startIndex, endIndex, dataRows));
    }

    /**
     * Recalculate the row data
     */
    public void update() {
        for (Section row : sections) {
            row.dataRows.update();
        }
    }

    /**
     * Get the number of rows
     *
     * @return The number of rows
     */
    public int getRowCount() {
        int sum = 0;
        for (Section row : sections) {
            sum += row.dataRows.getRowCount();
        }
        return sum;
    }

    /**
     * Is this the end of a section?
     *
     * @param rowIndex The row to check
     * @return True if this is the end of the section
     */
    public boolean isEndOfSection(int rowIndex) {
        if (rowIndex + 1 == getRowCount()) {
            return true;
        }
        return getSectionIndex(rowIndex + 1) == 0;
    }

    /**
     * Get the index inside of the section that the row resides
     *
     * @param rowIndex The row to get
     * @return The index inside of the section that the row resides
     */
    public int getSectionIndex(int rowIndex) {
        return rowIndex - getSection(rowIndex).startIndex;
    }

    /**
     * Get the DataRows that this row is in
     *
     * @param rowIndex The row to get
     * @return The DataRows this row is in
     */
    public DataRows getDataRow(int rowIndex) {
        return getSection(rowIndex).dataRows;
    }

    /**
     * Get the section that this row is in
     *
     * @param rowIndex The row to get
     * @return The section this row is in
     */
    private Section getSection(int rowIndex) {
        for (Section section : sections) {
            if (rowIndex <= section.endIndex) {
                return section;
            }
        }
        throw new RuntimeException("Imposable index");
    }

    /**
     * Section Enum
     */
    private class Section {
        Section(int startIndex, int endIndex, DataRows dataRows) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.dataRows = dataRows;
        }

        int startIndex;
        int endIndex;
        DataRows dataRows;
    }
}
