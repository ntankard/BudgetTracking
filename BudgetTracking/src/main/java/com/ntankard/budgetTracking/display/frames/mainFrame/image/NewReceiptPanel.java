package com.ntankard.budgetTracking.display.frames.mainFrame.image;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.receipt.Receipt;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewReceiptPanel extends UpdatableJPanel {

    /**
     * The data on the screen
     */
    private List<ImageToTransferPanel> imageToTransferPanels = new ArrayList<>();

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public NewReceiptPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        List<String> possibleImages = FileUtil.findFilesInDirectory(database.getFilesPath() + "\\Receipts");
        for (Receipt receipt : database.get(Receipt.class)) {
            boolean shouldRemove = false;
            for (String name : possibleImages) {
                if (receipt.getFileName().equals(name)) {
                    shouldRemove = true;
                    break;
                }
            }
            if (shouldRemove) {
                possibleImages.remove(receipt.getFileName());
            }
        }

        if (possibleImages.size() != 0) {
            JTabbedPane master_tPanel = new JTabbedPane();
            int i = 0;
            for (String image : possibleImages) {
                ImageToTransferPanel toAdd = new ImageToTransferPanel(database, image, this);
                imageToTransferPanels.add(toAdd);
                master_tPanel.addTab(i++ + "", toAdd);
            }
            this.add(master_tPanel, BorderLayout.CENTER);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        createUIComponents();
        imageToTransferPanels.forEach(ImageToTransferPanel::update);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyUpdate() {
        createUIComponents();
        super.notifyUpdate();
    }
}
