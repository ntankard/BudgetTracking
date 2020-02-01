package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Image;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;

import javax.swing.*;
import java.awt.*;

public class ReceiptPanel extends UpdatableJPanel {

    // Gui Component's
    private NewReceiptPanel newReceiptPanel;
    private ExistingReceiptPanel existingReceiptPanel;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public ReceiptPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        newReceiptPanel = new NewReceiptPanel(this);
        existingReceiptPanel = new ExistingReceiptPanel(this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("New", newReceiptPanel);
        master_tPanel.addTab("Loaded", existingReceiptPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        newReceiptPanel.update();
        existingReceiptPanel.update();
    }
}
