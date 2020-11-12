package com.ntankard.tracking.dispaly.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;

import javax.swing.*;
import java.awt.*;

public class SummaryGraphPanel extends UpdatableJPanel {

    // The GUI components
    private SavingsGraph savingsGraph;
    private CategoryGraph categoryGraph;
    private SetCategoryGraph setCategoryGraph;

    private JTabbedPane master_tPanel;

    /**
     * Constructor
     */
    public SummaryGraphPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridLayout(1, 1));

        savingsGraph = new SavingsGraph(this);
        categoryGraph = new CategoryGraph(this);
        setCategoryGraph = new SetCategoryGraph(this);

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
