package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedStatement;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets.PeriodPoolType_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.ExtendedStatementPanel;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.BankCategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.BankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.IntraCurrencyBankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PeriodSummary_StatementPanel extends UpdatableJPanel {

    // Core Data
    private Period core;
    private ExtendedStatement selectedStatement = null;

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private ExtendedStatementPanel statement_panel;
    private DataObject_DisplayList<BankCategoryTransfer> transaction_panel;
    private DynamicGUI_IntractableObject period_panel;
    private DynamicGUI_IntractableObject periodTotal_panel;
    private DataObject_DisplayList<StatementEnd> statementEnd_panel;
    private DataObject_DisplayList<BankTransfer> bankTransfer_panel;
    private DataObject_DisplayList<IntraCurrencyBankTransfer> intraCurrencyBankTransferDataObject_panel;

    private PeriodPoolType_Set<BankCategoryTransfer> transaction_panel_set;
    private BankCategoryTransfer_ElementController transaction_panel_controller;

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
            if (dataObject instanceof BankCategoryTransfer) {
                BankCategoryTransfer transaction = (BankCategoryTransfer) dataObject;
                if (selectedStatement != null) {
                    if (transaction.getSource().equals(selectedStatement.getBank())) {
                        rendererObject.background = Color.YELLOW;
                    }
                }
            }
        });

        statement_panel = new ExtendedStatementPanel(core, this);
        statement_panel.setComparator((o1, o2) -> {
            if (o1.getBank().getOrder().equals(o2.getBank().getOrder())) {
                return 0;
            } else if (o1.getBank().getOrder() > o2.getBank().getOrder()) {
                return 1;
            }
            return -1;
        });
        statement_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());

        transaction_panel_set = new PeriodPoolType_Set<>(core, null, BankCategoryTransfer.class);
        transaction_panel_controller = new BankCategoryTransfer_ElementController(core, this);

        JTabbedPane statementControl_panel = new JTabbedPane();

        bankTransfer_panel = new DataObject_DisplayList<>(BankTransfer.class, new Children_Set<>(BankTransfer.class, core), false, this);
        bankTransfer_panel.addControlButtons(new BankTransfer_ElementController(core, this));
        statementControl_panel.add("Transfers", bankTransfer_panel);

        intraCurrencyBankTransferDataObject_panel = new DataObject_DisplayList<>(IntraCurrencyBankTransfer.class, new Children_Set<>(IntraCurrencyBankTransfer.class, core), false, this);
        intraCurrencyBankTransferDataObject_panel.addControlButtons(new IntraCurrencyBankTransfer_ElementController(core, this));
        statementControl_panel.add("Currency Transfers", intraCurrencyBankTransferDataObject_panel);

        transaction_panel = new DataObject_DisplayList<>(BankCategoryTransfer.class, transaction_panel_set, false, this);
        transaction_panel.addControlButtons(transaction_panel_controller);
        statementControl_panel.add("Transactions", transaction_panel);

        statementEnd_panel = new DataObject_DisplayList<>(StatementEnd.class, new Children_Set<>(StatementEnd.class, core), false, this);
        statementEnd_panel.setComparator((o1, o2) -> {
            if (o1.getBank().getOrder().equals(o2.getBank().getOrder())) {
                return 0;
            } else if (o1.getBank().getOrder() > o2.getBank().getOrder()) {
                return 1;
            }
            return -1;
        });
        statementControl_panel.add("End Values", statementEnd_panel);

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
        this.add(statementControl_panel, summaryContainer_C);
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
        List selected = statement_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            selectedStatement = ((ExtendedStatement) selected.get(0));
            transaction_panel_controller.setBank(selectedStatement.getBank());
            transaction_panel_set.setPool(selectedStatement.getBank());
        } else {
            selectedStatement = null;
            transaction_panel_controller.setBank(null);
            transaction_panel_set.setPool(null);
        }

        // Update the UI
        periodSummary_panel.update();
        period_panel.update();
        periodTotal_panel.update();
        statementEnd_panel.update();
        bankTransfer_panel.update();
        intraCurrencyBankTransferDataObject_panel.update();
        transaction_panel.update();
    }
}
