package com.ntankard.budgetTracking.display.frames.mainFrame.image;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;

import javax.swing.*;
import java.awt.*;

public class ReceiptPanel extends UpdatableJPanel {

    // Gui Component's
    private NewReceiptPanel newReceiptPanel;
    private ExistingReceiptPanel existingReceiptPanel;

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public ReceiptPanel(Database database, Updatable master) {
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

        newReceiptPanel = new NewReceiptPanel(database, this);
        existingReceiptPanel = new ExistingReceiptPanel(database, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("New", newReceiptPanel);
        master_tPanel.addTab("Loaded", existingReceiptPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        newReceiptPanel.update();
        existingReceiptPanel.update();
    }
}
