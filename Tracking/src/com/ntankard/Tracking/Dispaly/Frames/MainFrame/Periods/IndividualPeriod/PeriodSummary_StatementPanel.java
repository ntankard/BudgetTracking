package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ExactChildren_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodPoolSet_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Table;
import com.ntankard.Tracking.Dispaly.Util.Comparators.BankSummary_Comparator;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.BankCategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.BankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryFundTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.IntraCurrencyBankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PeriodSummary_StatementPanel extends UpdatableJPanel {

    // Core Data
    private Period period;
    private Bank selectedBank = null;

    // The GUI components
    private PeriodSummary_Table periodSummary_Table_panel;

    private DataObject_DisplayList<Bank_Summary> bankSummary_panel;

    private BankCategoryTransfer_ElementController bankCategoryTransfer_controller;
    private PeriodPoolSet_Summary<BankCategoryTransfer> bankCategoryTransfer_set;
    private DataObject_DisplayList<BankCategoryTransfer> bankCategoryTransfer_panel;

    private DataObject_DisplayList<BankTransfer> bankTransfer_panel;
    private DataObject_DisplayList<IntraCurrencyBankTransfer> intraCurrencyBankTransfer_panel;
    private DataObject_DisplayList<CategoryFundTransfer> periodFundTransfer_panel;


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

        periodFundTransfer_panel = new DataObject_DisplayList<>(CategoryFundTransfer.class, new Children_Set<>(CategoryFundTransfer.class, period), false, this);
        periodFundTransfer_panel.addControlButtons(new CategoryFundTransfer_ElementController(period, this));
        statementControl_panel.add("Category Fund", periodFundTransfer_panel);

        bankTransfer_panel = new DataObject_DisplayList<>(BankTransfer.class, new ExactChildren_Set<>(BankTransfer.class, period), false, this);
        bankTransfer_panel.addControlButtons(new BankTransfer_ElementController(period, this));
        statementControl_panel.add("Transfers", bankTransfer_panel);

        intraCurrencyBankTransfer_panel = new DataObject_DisplayList<>(IntraCurrencyBankTransfer.class, new ExactChildren_Set<>(IntraCurrencyBankTransfer.class, period), false, this);
        intraCurrencyBankTransfer_panel.addControlButtons(new IntraCurrencyBankTransfer_ElementController(period, this));
        statementControl_panel.add("Currency Transfers", intraCurrencyBankTransfer_panel);

        // Statement summary -------------------------------------------------------------------------------------------

        bankSummary_panel = new DataObject_DisplayList<>(Bank_Summary.class, new ExactChildren_Set<>(Bank_Summary.class, period), false, this);
        bankSummary_panel.setComparator(new BankSummary_Comparator());
        bankSummary_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());

        // Statement transactions --------------------------------------------------------------------------------------

        bankCategoryTransfer_set = new PeriodPoolSet_Summary<>(period, null, BankCategoryTransfer.class);
        bankCategoryTransfer_controller = new BankCategoryTransfer_ElementController(period, this);
        bankCategoryTransfer_panel = new DataObject_DisplayList<>(BankCategoryTransfer.class, bankCategoryTransfer_set, false, this);
        bankCategoryTransfer_panel.addControlButtons(bankCategoryTransfer_controller);

        // Main layout -------------------------------------------------------------------------------------------------

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 3;
        summaryContainer_C.gridwidth = 3;
        this.add(periodSummary_Table_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;
        summaryContainer_C.weightx = 4;
        this.add(statementControl_panel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        summaryContainer_C.weightx = 10;
        this.add(bankSummary_panel, summaryContainer_C);

        summaryContainer_C.gridx = 2;
        summaryContainer_C.weightx = 1;
        this.add(bankCategoryTransfer_panel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        bankTransfer_panel.update();
        intraCurrencyBankTransfer_panel.update();
        periodFundTransfer_panel.update();

        updateTransactions();

        int max = bankSummary_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        int min = bankSummary_panel.getMainPanel().getListSelectionModel().getMaxSelectionIndex();
        bankSummary_panel.update();
        bankSummary_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
    }

    /**
     * Update the transaction and period section, not the statement section
     */
    private void updateTransactions() {
        // Find out if a statement has been selected
        List selected = bankSummary_panel.getMainPanel().getSelectedItems();
        if (selected.size() == 1) {
            selectedBank = ((Bank_Summary) selected.get(0)).getPool();
        } else {
            selectedBank = null;
        }

        // Update children on the selection
        bankCategoryTransfer_set.setPool(selectedBank);
        bankCategoryTransfer_controller.setBank(selectedBank);

        // Update the UI
        periodSummary_Table_panel.update();
        bankCategoryTransfer_panel.update();
    }
}
