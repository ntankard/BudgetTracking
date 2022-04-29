package com.ntankard.budgetTracking.display.frames.mainFrame;

import com.ntankard.budgetTracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.budgetTracking.display.util.elementControllers.FixedRecurringPayment_ElementController;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;

import java.awt.*;
import java.util.List;

public class RecurringPaymentPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<FixedRecurringPayment> fixedRecurringPayment_panel;
    private DataObject_DisplayList<RecurringBankTransfer> FixedRecurringTransfer_panel;
    private OneParent_Children_Set<RecurringBankTransfer, FixedRecurringPayment> fixedRecurringTransfer_set;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public RecurringPaymentPanel(Database database, Updatable master) {
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

        fixedRecurringPayment_panel = new DataObject_DisplayList<>(database.getSchema(), FixedRecurringPayment.class, new Full_Set<>(database, FixedRecurringPayment.class), false, this);
        fixedRecurringPayment_panel.addControlButtons(new FixedRecurringPayment_ElementController(database, this));
        fixedRecurringPayment_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> selectPayment());

        fixedRecurringTransfer_set = new OneParent_Children_Set<>(RecurringBankTransfer.class, null);
        FixedRecurringTransfer_panel = new DataObject_DisplayList<>(database.getSchema(), RecurringBankTransfer.class, fixedRecurringTransfer_set, false, this);

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;

        summaryContainer_C.gridx = 0;
        this.add(fixedRecurringPayment_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        this.add(FixedRecurringTransfer_panel, summaryContainer_C);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        if (fixedRecurringPayment_panel != null) {
            int max = fixedRecurringPayment_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            int min = fixedRecurringPayment_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            fixedRecurringPayment_panel.update();
            fixedRecurringPayment_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
        }
        selectPayment();
    }

    /**
     * How the transactions for the selected payment
     */
    private void selectPayment() {
        FixedRecurringPayment selectedBank = null;
        if (fixedRecurringPayment_panel != null) {
            List<?> selected = fixedRecurringPayment_panel.getMainPanel().getSelectedItems();
            if (selected.size() == 1) {
                selectedBank = ((FixedRecurringPayment) selected.get(0));
            }
        }

        fixedRecurringTransfer_set.setParent(selectedBank);
        FixedRecurringTransfer_panel.update();
    }
}
