package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.StatementPanel;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.Transaction_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;
import com.ntankard.Tracking.Dispaly.Util.Set.Children_Set;

import java.awt.*;
import java.util.List;

public class PeriodSummary_StatementPanel extends UpdatableJPanel {

    // Core Data
    private Period core;
    private Statement selectedStatement = null;

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private StatementPanel statement_panel;
    private DataObject_DisplayList<Transaction> transaction_panel;
    private DynamicGUI_IntractableObject period_panel;
    private DynamicGUI_IntractableObject periodTotal_panel;

    // StatementPanel Controllers stored to update the core objects
    private Children_Set<Transaction, Statement> transaction_panel_set;
    private Transaction_ElementController transaction_panel_controller;

    /**
     * Constructor
     */
    public PeriodSummary_StatementPanel(Period core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        periodSummary_panel = new PeriodSummary(core, false, this);
        periodSummary_panel.getModel().addCustomFormatter((dataObject, rendererObject) -> {
            if (dataObject instanceof Transaction) {
                Transaction transaction = (Transaction) dataObject;
                if (selectedStatement != null) {
                    if (transaction.getSourceContainer().equals(selectedStatement)) {
                        rendererObject.background = Color.YELLOW;
                    }
                }
            }
        });

        statement_panel = new StatementPanel(core, this);
        statement_panel.setComparator((o1, o2) -> {
            if (o1.getIdBank().getOrder() == o2.getIdBank().getOrder()) {
                return 0;
            } else if (o1.getIdBank().getOrder() > o2.getIdBank().getOrder()) {
                return 1;
            }
            return -1;
        });
        statement_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());

        transaction_panel_set = new Children_Set<>(Transaction.class, null);
        transaction_panel_controller = new Transaction_ElementController(selectedStatement, this);

        transaction_panel = new DataObject_DisplayList<>(Transaction.class, transaction_panel_set, this);
        transaction_panel.addControlButtons(transaction_panel_controller);

        period_panel = new DynamicGUI_IntractableObject<>(core, this);
        periodTotal_panel = new DynamicGUI_IntractableObject<>(new PeriodTransaction_Summary(core), this);

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 4;
        this.add(periodSummary_panel, summaryContainer_C);

        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        summaryContainer_C.weightx = 10;
        this.add(statement_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
        summaryContainer_C.weightx = 6;
        this.add(transaction_panel, summaryContainer_C);
        summaryContainer_C.gridx = 2;
        summaryContainer_C.weightx = 1;
        this.add(period_panel, summaryContainer_C);
        summaryContainer_C.gridx = 3;
        summaryContainer_C.weightx = 1;
        this.add(periodTotal_panel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        updateTransactions();

        int max = statement_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int min = statement_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        statement_panel.update();
        statement_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
    }

    /**
     * Update the transaction and period section, not the statement section
     */
    private void updateTransactions() {
        // Find out if a statement has been selected
        List<Statement> selected = statement_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            selectedStatement = selected.get(0);
        } else {
            selectedStatement = null;
        }

        // Update the core object of children
        transaction_panel_set.setCore(selectedStatement);
        transaction_panel_controller.setCore(selectedStatement);

        // Update the UI
        transaction_panel.update();
        periodSummary_panel.update();
        period_panel.update();
        periodTotal_panel.update();
    }
}
