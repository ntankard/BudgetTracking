package com.ntankard.budgetTracking.display.frames.mainFrame.statement;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.display.util.elementControllers.StatementTransactionAutoGroup_ElementController;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;

import java.awt.*;
import java.util.List;

public class GroupPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<Bank> bank_panel;
    private DataObject_DisplayList<StatementTransactionAutoGroup> autoGroup_panel;
    private OneParent_Children_Set<StatementTransactionAutoGroup, Bank> autoGroup_set;
    private StatementTransactionAutoGroup_ElementController autoGroup_elementController;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public GroupPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        bank_panel = new DataObject_DisplayList<>(database.getSchema(), Bank.class, new Full_Set<>(database, Bank.class), false, this);
        bank_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> selectBank());

        autoGroup_set = new OneParent_Children_Set<>(StatementTransactionAutoGroup.class, null);
        autoGroup_panel = new DataObject_DisplayList<>(database.getSchema(), StatementTransactionAutoGroup.class, autoGroup_set, false, this);
        autoGroup_elementController = new StatementTransactionAutoGroup_ElementController(database, this);
        autoGroup_panel.addControlButtons(autoGroup_elementController);

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;

        summaryContainer_C.gridx = 0;
        this.add(bank_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        this.add(autoGroup_panel, summaryContainer_C);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        if (bank_panel != null) {
            int max = bank_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            int min = bank_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            bank_panel.update();
            bank_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
        }
        selectBank();
    }

    /**
     * Grab the selected bank and update the UI
     */
    private void selectBank() {
        Bank selectedBank = null;
        if (bank_panel != null) {
            List<?> selected = bank_panel.getMainPanel().getSelectedItems();
            if (selected.size() == 1) {
                selectedBank = ((Bank) selected.get(0));
            }
        }

        autoGroup_set.setParent(selectedBank);
        autoGroup_elementController.setBank(selectedBank);
        autoGroup_panel.update();
    }
}
