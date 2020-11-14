package com.ntankard.tracking.dispaly.frames.mainFrame.image;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.Receipt;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;
import com.ntankard.tracking.util.swing.ImageJPanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExistingReceiptPanel extends UpdatableJPanel {

    /**
     * The data on the screen
     */
    private List<Receipt> receiptDisplayedData = new ArrayList<>();
    private List<BankTransfer> transferDisplayedData = new ArrayList<>();
    private Receipt selectedReceipt = null;

    // Gui Component's
    private JTable receipt_table;
    private AbstractTableModel receipt_table_model;

    private ImageJPanel imageJPanel;

    private JTable transfer_table;
    private AbstractTableModel transfer_table_model;
    private JComboBox<ExistingPeriod> period_combo = new JComboBox<>();
    private JComboBox<Currency> currency_combo = new JComboBox<>();
    private JComboBox<Bank> bank_combo = new JComboBox<>();
    private JButton associate_btn = new JButton("Associate");

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public ExistingReceiptPanel(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // Create Receipt Table
        receipt_table_model = new ReceiptTableModel();
        receipt_table = new JTable(receipt_table_model);
        receipt_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(receipt_table);
        this.add(scrollPane, BorderLayout.WEST);

        // Add the image
        imageJPanel = new ImageJPanel();
        this.add(imageJPanel, BorderLayout.CENTER);

        // Create Transfer Table
        transfer_table_model = new TransferTableModel();
        transfer_table = new JTable(transfer_table_model);
        transfer_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create the general side panel
        JPanel sidePanel = new JPanel(new GridBagLayout());
        this.add(sidePanel, BorderLayout.EAST);
        GridBagConstraints componentC = new GridBagConstraints();
        componentC.weightx = 2;
        componentC.fill = GridBagConstraints.BOTH;
        componentC.gridy = 0;

        // Create Period combo box panel
        JPanel period_panel = new JPanel();
        period_panel.setBorder(BorderFactory.createTitledBorder("Period"));
        period_panel.add(period_combo);
        for (ExistingPeriod period : trackingDatabase.get(ExistingPeriod.class))
            period_combo.addItem(period);
        period_combo.setSelectedItem(trackingDatabase.get(ExistingPeriod.class).get(trackingDatabase.get(ExistingPeriod.class).size() - 1));

        // Create Currency combo box panel
        JPanel currency_panel = new JPanel();
        currency_panel.setBorder(BorderFactory.createTitledBorder("Currency"));
        currency_panel.add(currency_combo);
        for (Currency currency : trackingDatabase.get(Currency.class))
            currency_combo.addItem(currency);

        // Create Bank combo box panel
        JPanel bank_panel = new JPanel();
        bank_panel.setBorder(BorderFactory.createTitledBorder("Bank"));
        bank_panel.add(bank_combo);
        populateBank();

        // Create Transfer list panel
        JPanel transfer_panel = new JPanel();
        transfer_panel.setBorder(BorderFactory.createTitledBorder("Transfer"));
        JScrollPane transfer_panel_scroll = new JScrollPane();
        transfer_panel_scroll.setViewportView(transfer_table);
        transfer_panel.add(transfer_table);

        // Add all to the side panel
        componentC.gridy++;
        sidePanel.add(period_panel, componentC);
        componentC.gridy++;
        sidePanel.add(currency_panel, componentC);
        componentC.gridy++;
        sidePanel.add(bank_panel, componentC);
        componentC.gridy++;
        sidePanel.add(transfer_panel, componentC);

        // Add space to move to top
        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = componentC.gridy + 1;
        sidePanel.add(new JSeparator(), spacerC);

        // Add the associate button
        componentC.gridy += 2;
        sidePanel.add(associate_btn, componentC);

        // Add listeners
        receipt_table.getSelectionModel().addListSelectionListener(e -> receiptSelected());
        transfer_table.getSelectionModel().addListSelectionListener(e -> transferSelected());
        period_combo.addActionListener(e -> populateBank());
        currency_combo.addActionListener(e -> populateBank());
        bank_combo.addActionListener(e -> populateTransfer());
        associate_btn.addActionListener(e -> associate());
    }

    /**
     * Populate the bank combo box based on the selected currency
     */
    private void populateBank() {
        Currency currency = (Currency) currency_combo.getSelectedItem();

        bank_combo.removeAllItems();
        for (Bank bank : (Objects.requireNonNull(currency)).getChildren(Bank.class)) {
            bank_combo.addItem(bank);
        }

        Bank defaultBank = trackingDatabase.getDefault(Bank.class);
        if (defaultBank.getCurrency().equals(currency)) {
            bank_combo.setSelectedItem(defaultBank);
        }

        populateTransfer();
    }

    /**
     * Populate the transfer list based on the other selected values
     */
    private void populateTransfer() {
        transferDisplayedData = new TwoParent_Children_Set<>(BankTransfer.class, (Bank) bank_combo.getSelectedItem(), (Period) period_combo.getSelectedItem()).get();
        transfer_table_model.fireTableDataChanged();
        associate_btn.setEnabled(false);
    }

    /**
     * Link the image to the selected transaction
     */
    private void associate() {
        if (selectedReceipt != null) {
            int index = transfer_table.getSelectionModel().getMaxSelectionIndex();
            BankTransfer transfer = transferDisplayedData.get(index);

            selectedReceipt.setBankTransfer(transfer);
        }
        notifyUpdate();
    }

    /**
     * Preform the necessary actions when a transfer is selected
     */
    private void transferSelected() {
        if (transfer_table.getSelectionModel().getMaxSelectionIndex() != -1) {
            associate_btn.setEnabled(true);
        }
    }

    /**
     * Preform the necessary actions when a receipt is selected
     */
    private void receiptSelected() {
        if (receipt_table.getSelectionModel().getMaxSelectionIndex() != -1) {

            // Update the image
            selectedReceipt = receiptDisplayedData.get(receipt_table.getSelectionModel().getMaxSelectionIndex());
            ImageIcon baseImage;
            baseImage = new ImageIcon(trackingDatabase.getImagePath() + selectedReceipt.getFileName());
            imageJPanel.setBaseImage(baseImage);

            // Update the selection panel
            period_combo.setSelectedItem(selectedReceipt.getBankTransfer().getPeriod());
            currency_combo.setSelectedItem(selectedReceipt.getBankTransfer().getCurrency());
            populateBank();
            bank_combo.setSelectedItem(selectedReceipt.getBankTransfer().getSource());
            populateTransfer();
            int index = transferDisplayedData.lastIndexOf(selectedReceipt.getBankTransfer());
            transfer_table.setRowSelectionInterval(index, index);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        receiptDisplayedData = trackingDatabase.get(Receipt.class);
        receipt_table_model.fireTableDataChanged();
        transfer_table_model.fireTableDataChanged();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Custom Models #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Model to access transfers
     */
    private class ReceiptTableModel extends AbstractTableModel {

        /**
         * {@inheritDoc
         */
        @Override
        public int getRowCount() {
            return receiptDisplayedData.size();
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
                    return receiptDisplayedData.get(rowIndex).getBankTransfer().getValue();
                case 1:
                    return receiptDisplayedData.get(rowIndex).getBankTransfer().getDescription();
                case 2:
                    return receiptDisplayedData.get(rowIndex).getBankTransfer().getDestination();
            }
            return null;
        }
    }

    /**
     * Model to access transfers
     */
    private class TransferTableModel extends AbstractTableModel {

        /**
         * {@inheritDoc
         */
        @Override
        public int getRowCount() {
            return transferDisplayedData.size();
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
                    return transferDisplayedData.get(rowIndex).getValue();
                case 1:
                    return transferDisplayedData.get(rowIndex).getDescription();
                case 2:
                    return transferDisplayedData.get(rowIndex).getDestination();
            }
            return null;
        }
    }
}
