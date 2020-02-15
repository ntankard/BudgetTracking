package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.CurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.UseCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Table;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CurrencyBankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.IntraCurrencyBankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.ManualBankCategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.UseCategoryFundTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;
import com.ntankard.Tracking.Dispaly.Util.Panels.Object_DisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PeriodSummary_StatementPanel extends UpdatableJPanel {

    // Core Data
    private Period period;
    private Bank selectedBank = null;

    // The GUI components
    private PeriodSummary_Table periodSummary_Table_panel;

    private Object_DisplayList<Bank_Summary> bankSummary_panel;
    private Object_DisplayList<FundEvent_Summary> fundEventSummary_panel;

    private ManualBankCategoryTransfer_ElementController bankCategoryTransfer_controller;
    private PeriodPool_SumSet<BankCategoryTransfer> bankCategoryTransfer_set;
    private DataObject_DisplayList<BankCategoryTransfer> bankCategoryTransfer_panel;

    private DataObject_DisplayList<UseCategoryFundTransfer> periodFundTransfer_panel;
    private DataObject_DisplayList<CurrencyBankTransfer> bankTransfer_panel;
    private DataObject_DisplayList<IntraCurrencyBankTransfer> intraCurrencyBankTransfer_panel;

    /**
     * Constructor
     */
    public PeriodSummary_StatementPanel(Period period, Updatable master) {
        super(master);
        this.period = period;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        // Main table --------------------------------------------------------------------------------------------------

        periodSummary_Table_panel = new PeriodSummary_Table(period, true, this);
        periodSummary_Table_panel.getModel().addCustomFormatter((dataObject, rendererObject) -> {
            if (dataObject instanceof BankCategoryTransfer) {
                BankCategoryTransfer transaction = (BankCategoryTransfer) dataObject;
                if (selectedBank != null) {
                    if (transaction.getSource().equals(selectedBank)) {
                        rendererObject.background = Color.YELLOW;
                    }
                }
            }
        });

        // Transfers ---------------------------------------------------------------------------------------------------

        JTabbedPane statementControl_panel = new JTabbedPane();

        periodFundTransfer_panel = new DataObject_DisplayList<>(UseCategoryFundTransfer.class, new Children_Set<>(UseCategoryFundTransfer.class, period), false, this);
        periodFundTransfer_panel.addControlButtons(new UseCategoryFundTransfer_ElementController(period, this));
        statementControl_panel.add("Category Fund", periodFundTransfer_panel);

        bankTransfer_panel = new DataObject_DisplayList<>(CurrencyBankTransfer.class, new Children_Set<>(CurrencyBankTransfer.class, period), false, this);
        bankTransfer_panel.addControlButtons(new CurrencyBankTransfer_ElementController(period, this));
        statementControl_panel.add("Transfers", bankTransfer_panel);

        intraCurrencyBankTransfer_panel = new DataObject_DisplayList<>(IntraCurrencyBankTransfer.class, new Children_Set<>(IntraCurrencyBankTransfer.class, period), false, this);
        intraCurrencyBankTransfer_panel.addControlButtons(new IntraCurrencyBankTransfer_ElementController(period, this));
        statementControl_panel.add("Currency Transfers", intraCurrencyBankTransfer_panel);

        // Statement summary -------------------------------------------------------------------------------------------

        JTabbedPane summary_panel = new JTabbedPane();

        if (period instanceof ExistingPeriod) {
            bankSummary_panel = new Object_DisplayList<>(Bank_Summary.class, new BankSummary_Set((ExistingPeriod) period), false, this);
            bankSummary_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());
            summary_panel.add("Bank", bankSummary_panel);
        }

        fundEventSummary_panel = new Object_DisplayList<>(FundEvent_Summary.class, new FundEventSummary_Set(period), false, this);
        summary_panel.add("Fund Event", fundEventSummary_panel);

        // Statement transactions --------------------------------------------------------------------------------------

        bankCategoryTransfer_set = new PeriodPool_SumSet<>(BankCategoryTransfer.class, period, null);
        bankCategoryTransfer_controller = new ManualBankCategoryTransfer_ElementController(period, this);
        bankCategoryTransfer_panel = new DataObject_DisplayList<>(BankCategoryTransfer.class, bankCategoryTransfer_set, false, this);
        bankCategoryTransfer_panel.addControlButtons(bankCategoryTransfer_controller);

        // Main layout -------------------------------------------------------------------------------------------------

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 2;
        summaryContainer_C.gridwidth = 3;
        this.add(periodSummary_Table_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;
        summaryContainer_C.weightx = 4;
        this.add(statementControl_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        summaryContainer_C.weightx = 10;
        this.add(summary_panel, summaryContainer_C);

        summaryContainer_C.gridx = 2;
        summaryContainer_C.weightx = 1;
        this.add(bankCategoryTransfer_panel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodFundTransfer_panel.update();
        bankTransfer_panel.update();
        intraCurrencyBankTransfer_panel.update();

        updateTransactions();

        if (bankSummary_panel != null) {
            int max = bankSummary_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            int min = bankSummary_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
            bankSummary_panel.update();
            bankSummary_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
        }
        fundEventSummary_panel.update();
    }

    /**
     * Update the transaction and period section, not the statement section
     */
    private void updateTransactions() {
        // Find out if a statement has been selected
        selectedBank = null;
        if (bankSummary_panel != null) {
            List selected = bankSummary_panel.getMainPanel().getSelectedItems();
            if (selected.size() == 1) {
                selectedBank = ((Bank_Summary) selected.get(0)).getPool();
            }
        }

        // Update children on the selection
        bankCategoryTransfer_set.setPool(selectedBank);
        bankCategoryTransfer_controller.setBank(selectedBank);

        // Update the UI
        periodSummary_Table_panel.update();
        bankCategoryTransfer_panel.update();
    }
}
