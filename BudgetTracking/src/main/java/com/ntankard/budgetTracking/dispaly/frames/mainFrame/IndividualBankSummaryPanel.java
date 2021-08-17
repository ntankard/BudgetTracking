package com.ntankard.budgetTracking.dispaly.frames.mainFrame;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Bank_Summary;
import com.ntankard.budgetTracking.dispaly.util.panels.Object_DisplayList;

import java.awt.*;

public class IndividualBankSummaryPanel extends UpdatableJPanel {

    // Core database
    private final Database database;

    private final Bank bank;
    private Object_DisplayList<Bank_Summary> bankSummary_panel;

    /**
     * Constructor
     */
    public IndividualBankSummaryPanel(Database database, Bank bank, Updatable master) {
        super(master);
        this.database = database;
        this.bank = bank;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        bankSummary_panel = new Object_DisplayList<>(database.getSchema(), Bank_Summary.class, new OneParent_Children_Set<>(Bank_Summary.class, (Pool)bank), false, this);

       this.add(bankSummary_panel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        bankSummary_panel.update();
    }
}
