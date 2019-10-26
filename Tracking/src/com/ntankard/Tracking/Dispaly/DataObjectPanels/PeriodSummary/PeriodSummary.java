package com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Table.TableColumnAdjuster;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;

import javax.swing.*;
import java.awt.*;

public class PeriodSummary extends UpdatableJScrollPane {

    // Core Data
    private Period core;
    private boolean addTransfers;

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
     */
    public PeriodSummary(Period core, boolean addTransfers, Updatable master) {
        super(master);
        this.core = core;
        this.addTransfers = addTransfers;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        model = new PeriodSummary_Model(core, addTransfers);

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
     * Get the model driving the table
     *
     * @return The model driving the table
     */
    public PeriodSummary_Model getModel() {
        return model;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        model.update();
    }
}
