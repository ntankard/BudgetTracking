package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Image;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewReceiptPanel extends UpdatableJPanel {

    /**
     * The data on the screen
     */
    private List<ImageToTransferPanel> imageToTransferPanels = new ArrayList<>();

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public NewReceiptPanel(Updatable master) {
        super(master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        List<String> possibleImages = FileUtil.findFilesInDirectory(TrackingDatabase.get().getImagePath());
        for (Receipt receipt : TrackingDatabase.get().get(Receipt.class)) {
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
                ImageToTransferPanel toAdd = new ImageToTransferPanel(image, this);
                imageToTransferPanels.add(toAdd);
                master_tPanel.addTab(i++ + "", toAdd);
            }
            this.add(master_tPanel, BorderLayout.CENTER);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        createUIComponents();
        imageToTransferPanels.forEach(ImageToTransferPanel::update);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyUpdate() {
        createUIComponents();
        super.notifyUpdate();
    }
}
