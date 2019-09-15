package com.ntankard.Tracking.Frames.Swing.PeriodSummary;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.TableColumnAdjuster;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;

public class PeriodSummary extends UpdatableJScrollPane {

    // Core Data
    private TrackingDatabase trackingDatabase;
    private Period core;

    /**
     * The model driving the table
     */
    private PeriodSummary_Model model;

    /**
     * TableColumnAdjuster used to shrink the table
     */
    private TableColumnAdjuster tableColumnAdjuster;

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     * @param master           The parent of this frame
     */
    public PeriodSummary(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        this.core = core;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        model = new PeriodSummary_Model(trackingDatabase, core);

        JTable table = new JTable(model);

        // Allow custom borders in the render
        table.setDefaultRenderer(Object.class, new PeriodSummary_Renderer());
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Shrink the table to minimum size
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableColumnAdjuster = new TableColumnAdjuster(table);

        this.setViewportView(table);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        model.update();
        tableColumnAdjuster.adjustColumns();
    }
}
