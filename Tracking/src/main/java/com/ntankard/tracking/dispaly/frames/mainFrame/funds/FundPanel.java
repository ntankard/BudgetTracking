package com.ntankard.tracking.dispaly.frames.mainFrame.funds;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;

import javax.swing.*;
import java.awt.*;

public class FundPanel extends UpdatableJPanel {

    // The GUI components
    private FundTabPanel fundTabPanel;
    private FundEventsPanel fundEventsPanel;

    /**
     * Constructor
     */
    public FundPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JTabbedPane master_tPanel = new JTabbedPane();

        fundTabPanel = new FundTabPanel(this);
        master_tPanel.addTab("Fund Summary", fundTabPanel);

        fundEventsPanel = new FundEventsPanel(this);
        master_tPanel.addTab("List", fundEventsPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        fundTabPanel.update();
        fundEventsPanel.update();
    }
}
