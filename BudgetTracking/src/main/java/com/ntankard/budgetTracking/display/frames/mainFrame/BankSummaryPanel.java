package com.ntankard.budgetTracking.display.frames.mainFrame;

import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BankSummaryPanel extends UpdatableJPanel {

    // Core database
    private final Database database;
    private final List<IndividualBankSummaryPanel> bankPanels = new ArrayList<>();

    /**
     * Constructor
     */
    public BankSummaryPanel(Database database, Updatable master) {
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

        for (Bank bank : database.get(Bank.class)) {
            IndividualBankSummaryPanel individualBankSummaryPanel = new IndividualBankSummaryPanel(database, bank, this);
            bankPanels.add(individualBankSummaryPanel);
            master_tPanel.addTab(bank.getName(), individualBankSummaryPanel);
        }

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        for (IndividualBankSummaryPanel individualBankSummaryPanel : bankPanels) {
            individualBankSummaryPanel.update();
        }
    }
}
