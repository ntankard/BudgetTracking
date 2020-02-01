package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Image;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Util.Swing.ImageJPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExistingReceiptPanel extends UpdatableJPanel implements ListSelectionListener {

    /**
     * The data on the screen
     */
    private List<Receipt> displayedData = new ArrayList<>();

    // Gui Component's
    private JTable transfer_table;
    private AbstractTableModel transfer_table_model;
    private ImageJPanel imageJPanel;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public ExistingReceiptPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // Create Transfer Table
        transfer_table_model = new TransferTableModel();
        transfer_table = new JTable(transfer_table_model);
        transfer_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transfer_table.getSelectionModel().addListSelectionListener(this);

        // Add the table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(transfer_table);
        this.add(scrollPane, BorderLayout.WEST);

        // Add the image
        imageJPanel = new ImageJPanel();
        this.add(imageJPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (transfer_table.getSelectionModel().getMaxSelectionIndex() != -1) {
            Receipt receipt = displayedData.get(transfer_table.getSelectionModel().getMaxSelectionIndex());
            ImageIcon baseImage;
            if (receipt.isFirstFile()) {
                baseImage = new ImageIcon(TrackingDatabase.get().getNewImagePath() + receipt.getFileName());
            } else {
                baseImage = new ImageIcon(TrackingDatabase.get().getSavedImagePath() + receipt.getFileName());
            }
            imageJPanel.setBaseImage(baseImage);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        displayedData = TrackingDatabase.get().get(Receipt.class);
        transfer_table_model.fireTableDataChanged();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Custom Models #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Model to access transfers
     */
    private class TransferTableModel extends AbstractTableModel {

        /**
         * {@inheritDoc
         */
        @Override
        public int getRowCount() {
            return displayedData.size();
        }

        /**
         * {@inheritDoc
         */
        @Override
        public int getColumnCount() {
            return 3;
        }

        /**
         * {@inheritDoc
         */
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Value";
                case 1:
                    return "Description";
                case 2:
                    return "Category";
            }
            return null;
        }

        /**
         * {@inheritDoc
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return displayedData.get(rowIndex).getBankCategoryTransfer().getDestinationValue();
                case 1:
                    return displayedData.get(rowIndex).getBankCategoryTransfer().getDescription();
                case 2:
                    return displayedData.get(rowIndex).getBankCategoryTransfer().getDestination();
            }
            return null;
        }
    }
}
