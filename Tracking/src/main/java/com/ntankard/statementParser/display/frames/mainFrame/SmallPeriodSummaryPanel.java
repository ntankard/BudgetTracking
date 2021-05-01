package com.ntankard.statementParser.display.frames.mainFrame;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;
import com.ntankard.statementParser.dataBase.BankAccount;
import com.ntankard.statementParser.dataBase.StatementFolder;
import com.ntankard.statementParser.dataBase.TransactionPeriod;
import com.ntankard.statementParser.dataBase.transactionGroup.TransactionGroup;
import com.ntankard.tracking.dispaly.util.panels.DataObject_DisplayList;

import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class SmallPeriodSummaryPanel extends UpdatableJPanel {

    // Core database
    private final Database database;

    private ListSelectionListener selectionListener;

    private DataObject_DisplayList<BankAccount> saveInstance_panel;
    private DataObject_DisplayList<StatementFolder> savedDataObject_panel;
    private DataObject_DisplayList<TransactionGroup> savedLine_panel;

    private OneParent_Children_Set<StatementFolder, BankAccount> savedDataObject_set;
    private TwoParent_Children_Set<TransactionGroup, BankAccount, TransactionPeriod> savedLine_set;

    private BankAccount saveInstance_selected = null;
    private StatementFolder file_selected = null;

    /**
     * Constructor
     */
    public SmallPeriodSummaryPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
        update();
        int size = database.get(BankAccount.class).size();
        saveInstance_panel.getMainPanel().getListSelectionModel().setSelectionInterval(size - 1, size - 1);
        update();
        int secondSize = saveInstance_selected.getChildren(StatementFolder.class).size();
        savedDataObject_panel.getMainPanel().getListSelectionModel().setSelectionInterval(secondSize - 1, secondSize - 1);
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        selectionListener = e -> update();

        saveInstance_panel = new DataObject_DisplayList<>(database.getSchema(), BankAccount.class, new Full_Set<>(database, BankAccount.class), false, this);
        saveInstance_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);

        savedDataObject_set = new OneParent_Children_Set<>(StatementFolder.class, null);
        savedDataObject_panel = new DataObject_DisplayList<>(database.getSchema(), StatementFolder.class, savedDataObject_set, false, this);
        savedDataObject_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);

        savedLine_set = new TwoParent_Children_Set<>(TransactionGroup.class, null, null);
        savedLine_panel = new DataObject_DisplayList<>(database.getSchema(), TransactionGroup.class, savedLine_set, false, this);

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;

        summaryContainer_C.gridx = 0;
        this.add(saveInstance_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        summaryContainer_C.weightx = 5;
        this.add(savedDataObject_panel, summaryContainer_C);

        summaryContainer_C.gridx = 2;
        summaryContainer_C.weightx = 10;
        this.add(savedLine_panel, summaryContainer_C);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        // Turn off the listeners to prevent a infinite loop
        saveInstance_panel.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);
        savedDataObject_panel.getMainPanel().getListSelectionModel().removeListSelectionListener(selectionListener);

        // Find out the current status of the lists
        int saveInstance_panel_max = saveInstance_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int saveInstance_panel_min = saveInstance_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int savedDataObject_panel_max = savedDataObject_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int savedDataObject_panel_min = savedDataObject_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();

        // Update the SaveInstance panel and select the same element as before
        saveInstance_panel.update();
        saveInstance_panel.getMainPanel().getListSelectionModel().setSelectionInterval(saveInstance_panel_max, saveInstance_panel_min);

        // Find the selected SaveInstance
        BankAccount newSaveInstance_selected = null;
        List<?> selected = saveInstance_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            newSaveInstance_selected = ((BankAccount) selected.get(0));
        }

        // If the selected SaveInstance was changed clear everything below
        if (newSaveInstance_selected != saveInstance_selected) {
            saveInstance_selected = newSaveInstance_selected;
            savedDataObject_set.setParent(saveInstance_selected);
            savedDataObject_panel_max = -1;
            savedDataObject_panel_min = -1;
        }

        // Update the SavedDataObject panel and select the same element as before
        savedDataObject_panel.update();
        savedDataObject_panel.getMainPanel().getListSelectionModel().setSelectionInterval(savedDataObject_panel_max, savedDataObject_panel_min);

        // Find the selected SavedDataObject
        StatementFolder newFile_selected = null;
        selected = savedDataObject_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            newFile_selected = ((StatementFolder) selected.get(0));
        }

        // If the selected SavedDataObject was changed clear everything below
        if (newFile_selected != file_selected) {
            file_selected = newFile_selected;
            //savedLine_set.setParent(file_selected);
            if (file_selected != null) {
                savedLine_set.setPrimaryParent(file_selected.getBankAccount());
                savedLine_set.setSecondaryParent(file_selected.getTransactionPeriod());
            }
        }

        // Update the SavedLine panel and select the same element as before
        savedLine_panel.update();

        // Turn on the listeners
        saveInstance_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
        savedDataObject_panel.getMainPanel().getListSelectionModel().addListSelectionListener(selectionListener);
    }
}