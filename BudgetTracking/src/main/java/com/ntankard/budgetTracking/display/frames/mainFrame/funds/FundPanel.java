package com.ntankard.budgetTracking.display.frames.mainFrame.funds;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;

import javax.swing.*;
import java.awt.*;

public class FundPanel extends UpdatableJPanel {

    // The GUI components
    private FundTabPanel fundTabPanel;
    private FundEventsPanel fundEventsPanel;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public FundPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JTabbedPane master_tPanel = new JTabbedPane();

        fundTabPanel = new FundTabPanel(database, this);
        master_tPanel.addTab("Fund Summary", fundTabPanel);

        fundEventsPanel = new FundEventsPanel(database, this);
        master_tPanel.addTab("List", fundEventsPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        fundTabPanel.update();
        fundEventsPanel.update();
    }
}
