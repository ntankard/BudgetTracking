package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.FixedRecurringPayment_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import java.awt.*;
import java.util.List;

public class RecurringPaymentPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<FixedRecurringPayment> fixedRecurringPayment_panel;
    private DataObject_DisplayList<RecurringBankTransfer> FixedRecurringTransfer_panel;
    private OneParent_Children_Set<RecurringBankTransfer, FixedRecurringPayment> fixedRecurringTransfer_set;

    /**
     * Constructor
     */
    public RecurringPaymentPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        fixedRecurringPayment_panel = new DataObject_DisplayList<>(FixedRecurringPayment.class, new Full_Set<>(FixedRecurringPayment.class), false, this);
        fixedRecurringPayment_panel.addControlButtons(new FixedRecurringPayment_ElementController(this));
        fixedRecurringPayment_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> selectPayment());

        fixedRecurringTransfer_set = new OneParent_Children_Set<>(RecurringBankTransfer.class, null);
        FixedRecurringTransfer_panel = new DataObject_DisplayList<>(RecurringBankTransfer.class, fixedRecurringTransfer_set, false, this);

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
     * {@inheritDoc
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
