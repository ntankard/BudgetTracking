package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Image;

import com.ntankard.dynamicGUI.Gui.Util.Decoder.Decoder;
import com.ntankard.dynamicGUI.Gui.Util.Decoder.DoubleDecoder;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.ManualBankTransfer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;
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
    private List<BankTransfer> displayedData = new ArrayList<>();

    // Gui components
    private JTable transfer_table;
    private JComboBox<ExistingPeriod> period_combo = new JComboBox<>();
    private JComboBox<Currency> currency_combo = new JComboBox<>();
    private JComboBox<Bank> bank_combo = new JComboBox<>();
    private AbstractTableModel transfer_table_model;
    private JButton associate_btn = new JButton("Associate");

    private JButton new_btn = new JButton("New");
    private JTextField description_txt = new JTextField();
    private JTextField price_txt = new JFormattedTextField();
    private JComboBox<SolidCategory> category_combo = new JComboBox<>();

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
        ImageIcon baseImage = new ImageIcon(TrackingDatabase.get().getImagePath() + imagePath);
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
        componentC.weightx = 2;
        componentC.fill = GridBagConstraints.BOTH;
        componentC.gridy = 0;

        // Create Period combo box panel
        JPanel period_panel = new JPanel();
        period_panel.setBorder(BorderFactory.createTitledBorder("Period"));
        period_panel.add(period_combo);
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class))
            period_combo.addItem(period);
        period_combo.setSelectedItem(TrackingDatabase.get().get(ExistingPeriod.class).get(TrackingDatabase.get().get(ExistingPeriod.class).size() - 1));

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

        // Create Description field panel
        JPanel description_panel = new JPanel(new BorderLayout());
        description_panel.setBorder(BorderFactory.createTitledBorder("Description"));
        description_panel.add(description_txt, BorderLayout.CENTER);

        // Create Price field panel
        JPanel price_panel = new JPanel(new BorderLayout());
        price_panel.setBorder(BorderFactory.createTitledBorder("Cost"));
        price_panel.add(price_txt, BorderLayout.CENTER);

        // Create Category combo panel
        JPanel category_panel = new JPanel(new BorderLayout());
        category_panel.setBorder(BorderFactory.createTitledBorder("Category"));
        category_panel.add(category_combo, BorderLayout.CENTER);

        // Add all to the side panel
        componentC.gridy++;
        sidePanel.add(period_panel, componentC);
        componentC.gridy++;
        sidePanel.add(currency_panel, componentC);
        componentC.gridy++;
        sidePanel.add(bank_panel, componentC);
        componentC.gridy++;
        sidePanel.add(transfer_panel, componentC);
        componentC.gridy++;
        sidePanel.add(description_panel, componentC);
        componentC.gridy++;
        sidePanel.add(price_panel, componentC);
        componentC.gridy++;
        sidePanel.add(category_panel, componentC);

        // Add space to move to top
        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = componentC.gridy + 1;
        sidePanel.add(new JSeparator(), spacerC);

        // Add the associate button
        componentC.gridy += 2;
        sidePanel.add(associate_btn, componentC);
        componentC.gridy += 2;
        sidePanel.add(new_btn, componentC);

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
        associate_btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                associate();
            }
        });
        new_btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newTransaction();
            }
        });
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

        Bank defaultBank = TrackingDatabase.get().getDefault(Bank.class);
        if (defaultBank.getCurrency().equals(currency)) {
            bank_combo.setSelectedItem(defaultBank);
        }

        populateTransfer();
    }

    /**
     * Populate the transfer list based on the other selected values
     */
    private void populateTransfer() {
        displayedData = new TwoParent_Children_Set<>(BankTransfer.class, (Bank) bank_combo.getSelectedItem(), (Period) period_combo.getSelectedItem()).get();
        transfer_table_model.fireTableDataChanged();
        associate_btn.setEnabled(false);
    }

    /**
     * Link the image to the selected transaction
     */
    private void associate() {
        int index = transfer_table.getSelectionModel().getMaxSelectionIndex();
        BankTransfer transfer = displayedData.get(index);

        Receipt receipt = Receipt.make(TrackingDatabase.get().getNextId(), imagePath, transfer);
        receipt.add();

        notifyUpdate();
    }

    private void newTransaction() {
        Decoder<Double> decoder = new DoubleDecoder(2);
        Double cost;
        try {
            cost = decoder.encode(price_txt.getText());
        } catch (NumberFormatException e) {
            return;
        }
        Bank bank = (Bank) bank_combo.getSelectedItem();
        Period period = (Period) period_combo.getSelectedItem();
        String description = description_txt.getText();
        SolidCategory solidCategory = (SolidCategory) category_combo.getSelectedItem();

        ManualBankTransfer manualBankTransferN = ManualBankTransfer.make(TrackingDatabase.get().getNextId(), description, period, bank, cost, null, solidCategory, null);
        manualBankTransferN.add();

        Receipt receipt = Receipt.make(TrackingDatabase.get().getNextId(), imagePath, manualBankTransferN);
        receipt.add();

        notifyUpdate();

    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (transfer_table.getSelectionModel().getMaxSelectionIndex() != -1) {
            associate_btn.setEnabled(true);
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        populateBank();

        category_combo.removeAllItems();
        for (SolidCategory solidCategory : TrackingDatabase.get().get(SolidCategory.class)) {
            category_combo.addItem(solidCategory);
        }
        category_combo.setSelectedItem(TrackingDatabase.get().getDefault(SolidCategory.class));

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
                    return displayedData.get(rowIndex).getValue();
                case 1:
                    return displayedData.get(rowIndex).getDescription();
                case 2:
                    return displayedData.get(rowIndex).getDestination();
            }
            return null;
        }
    }
}
