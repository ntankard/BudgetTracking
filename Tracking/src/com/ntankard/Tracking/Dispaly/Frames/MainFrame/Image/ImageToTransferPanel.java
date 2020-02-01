package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Image;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.Util.Swing.ImageJPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageToTransferPanel extends UpdatableJPanel implements ListSelectionListener {

    // Core data
    private String imagePath;
    private List<BankCategoryTransfer> displayedData = new ArrayList<>();

    // Gui components
    private JTable transfer_table;
    private JComboBox<ExistingPeriod> period_combo = new JComboBox<>();
    private JComboBox<Currency> currency_combo = new JComboBox<>();
    private JComboBox<Bank> bank_combo = new JComboBox<>();
    private AbstractTableModel transfer_table_model;
    private JButton associateButton = new JButton("Associate");

    /**
     * Constructor
     */
    public ImageToTransferPanel(String imagePath, Updatable master) {
        super(master);
        this.imagePath = imagePath;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // Load the image
        ImageIcon baseImage = new ImageIcon(TrackingDatabase.get().getNewImagePath() + imagePath);
        this.add(new ImageJPanel(baseImage), BorderLayout.CENTER);

        // Create Transfer Table
        transfer_table_model = new TransferTableModel();
        transfer_table = new JTable(transfer_table_model);
        transfer_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transfer_table.getSelectionModel().addListSelectionListener(this);

        // Create the general side panel
        JPanel sidePanel = new JPanel(new GridBagLayout());
        this.add(sidePanel, BorderLayout.EAST);
        GridBagConstraints componentC = new GridBagConstraints();
        componentC.weightx = 1;
        componentC.fill = GridBagConstraints.BOTH;
        componentC.gridy = 0;

        // Create Period combo box panel
        JPanel period_panel = new JPanel();
        period_panel.setBorder(BorderFactory.createTitledBorder("Period"));
        period_panel.add(period_combo);
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class))
            period_combo.addItem(period);

        // Create Currency combo box panel
        JPanel currency_panel = new JPanel();
        currency_panel.setBorder(BorderFactory.createTitledBorder("Currency"));
        currency_panel.add(currency_combo);
        for (Currency currency : TrackingDatabase.get().get(Currency.class))
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
        sidePanel.add(associateButton, componentC);

        // Add listeners
        period_combo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateBank();
            }
        });
        currency_combo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateBank();
            }
        });
        bank_combo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateTransfer();
            }
        });
        associateButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                associate();
            }
        });
    }

    /**
     * Populate the bank combo box based on the selected currency
     */
    private void populateBank() {
        bank_combo.removeAllItems();
        for (Bank bank : ((Currency) Objects.requireNonNull(currency_combo.getSelectedItem())).getChildren(Bank.class)) {
            if (new MultiParent_Set<>(BankCategoryTransfer.class, bank, (ExistingPeriod) period_combo.getSelectedItem()).get().size() != 0) {
                bank_combo.addItem(bank);
            }
        }

        populateTransfer();
    }

    /**
     * Populate the transfer list based on the other selected values
     */
    private void populateTransfer() {
        displayedData = new MultiParent_Set<>(BankCategoryTransfer.class, (Bank) bank_combo.getSelectedItem(), (Period) period_combo.getSelectedItem()).get();
        transfer_table_model.fireTableDataChanged();
        associateButton.setEnabled(false);
    }

    /**
     * Link the image to the selected transaction
     */
    private void associate() {
        int index = transfer_table.getSelectionModel().getMaxSelectionIndex();
        BankCategoryTransfer transfer = displayedData.get(index);

        Receipt receipt = new Receipt(TrackingDatabase.get().getNextId(), imagePath, transfer);
        receipt.setFirstFile(true);
        receipt.add();

        TrackingDatabase.get().getPossibleImages().remove(imagePath);

        notifyUpdate();

    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (transfer_table.getSelectionModel().getMaxSelectionIndex() != -1) {
            associateButton.setEnabled(true);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        populateBank();
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
                    return displayedData.get(rowIndex).getDestinationValue();
                case 1:
                    return displayedData.get(rowIndex).getDescription();
                case 2:
                    return displayedData.get(rowIndex).getDestination();
            }
            return null;
        }
    }
}
