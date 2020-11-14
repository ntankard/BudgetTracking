package com.ntankard.tracking.dispaly.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

import javax.swing.*;
import java.awt.*;

public class SummaryGraphPanel extends UpdatableJPanel {

    // The GUI components
    private SavingsGraph savingsGraph;
    private CategoryGraph categoryGraph;
    private SetCategoryGraph setCategoryGraph;

    private JTabbedPane master_tPanel;

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Constructor
     */
    public SummaryGraphPanel(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridLayout(1, 1));

        savingsGraph = new SavingsGraph(trackingDatabase, this);
        categoryGraph = new CategoryGraph(trackingDatabase, this);
        setCategoryGraph = new SetCategoryGraph(trackingDatabase, this);

        master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Savings", savingsGraph);
        master_tPanel.addTab("Category", categoryGraph);
        master_tPanel.addTab("SetCategory", setCategoryGraph);

        this.add(master_tPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        savingsGraph.update();
        categoryGraph.update();
        setCategoryGraph.update();
    }
}
