package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.awt.*;

public class SummaryGraphPanel extends UpdatableJPanel {

    // The GUI components
    private SavingsGraph savingsGraph;

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

        this.add(savingsGraph);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        savingsGraph.update();
    }
}
