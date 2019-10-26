package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;
import java.awt.*;

public class SummaryGraphPanel extends UpdatableJPanel {

    // The GUI components
    private SavingsGraph savingsGraph;
    private CategoryGraph categoryGraph;

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

        master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Savings", savingsGraph);
        master_tPanel.addTab("Category", categoryGraph);

        this.add(master_tPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        savingsGraph.update();
    }
}
