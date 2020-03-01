package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Table;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.ManualBankTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.ManualFundTransfer_ElementController;
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

    private ManualBankTransfer_ElementController bankCategoryTransfer_controller;
    private TwoParent_Children_Set<BankTransfer, Period, Bank> bankCategoryTransfer_set;
    private DataObject_DisplayList<BankTransfer> bankCategoryTransfer_panel;

    private DataObject_DisplayList<FundTransfer> periodFundTransfer_panel;

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

            if (dataObject instanceof HalfTransfer) {
                if (((HalfTransfer) dataObject).getTransfer() instanceof BankTransfer) {

                    // Check if its selected
                    boolean selected = false;
                    BankTransfer transaction = (BankTransfer) ((HalfTransfer) dataObject).getTransfer();
                    if (selectedBank != null) {
                        selected = transaction.getSource().equals(selectedBank);
                    }

                    if (((HalfTransfer) dataObject).getTransfer() instanceof RecurringBankTransfer) {
                        if (((HalfTransfer) dataObject).getTransfer().getChildren(Receipt.class).size() != 0) {
                            // FixedRecurringTransfer with receipt
                            if (selected) {
                                rendererObject.background = new Color(255, 102, 0);
                            } else {
                                rendererObject.background = new Color(255, 225, 204);
                            }
                        } else {
                            // FixedRecurringTransfer without receipt
                            if (selected) {
                                rendererObject.background = new Color(255, 213, 0);
                            } else {
                                rendererObject.background = new Color(255, 247, 204);
                            }
                        }
                    } else {
                        if (((HalfTransfer) dataObject).getTransfer().getChildren(Receipt.class).size() != 0) {
                            // Regular with receipt
                            if (selected) {
                                rendererObject.background = new Color(255, 0, 0);
                            } else {
                                rendererObject.background = new Color(255, 204, 204);
                            }
                        } else {
                            if (selected) {
                                rendererObject.background = new Color(255, 255, 0);
                            }
                        }
                    }
                } else if (((HalfTransfer) dataObject).getTransfer() instanceof FundTransfer) {
                    if (((HalfTransfer) dataObject).getTransfer() instanceof RePayFundTransfer) {
                        rendererObject.background = new Color(204, 238, 255);
                    }
                }
            }
        });

        // Transfers ---------------------------------------------------------------------------------------------------

        periodFundTransfer_panel = new DataObject_DisplayList<>(FundTransfer.class, new OneParent_Children_Set<>(FundTransfer.class, period), false, this);
        periodFundTransfer_panel.addControlButtons(new ManualFundTransfer_ElementController(period, this));

        // Statement summary -------------------------------------------------------------------------------------------

        if (period instanceof ExistingPeriod) {
            bankSummary_panel = new Object_DisplayList<>(Bank_Summary.class, new BankSummary_Set((ExistingPeriod) period), false, this);
            bankSummary_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());
        }

        fundEventSummary_panel = new Object_DisplayList<>(FundEvent_Summary.class, new FundEventSummary_Set(period), false, this);

        // Statement transactions --------------------------------------------------------------------------------------

        bankCategoryTransfer_set = new TwoParent_Children_Set<>(BankTransfer.class, period, null);
        bankCategoryTransfer_controller = new ManualBankTransfer_ElementController(period, this);
        bankCategoryTransfer_panel = new DataObject_DisplayList<>(BankTransfer.class, bankCategoryTransfer_set, false, this);
        bankCategoryTransfer_panel.addControlButtons(bankCategoryTransfer_controller);

        // Main layout -------------------------------------------------------------------------------------------------

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 2;
        summaryContainer_C.gridwidth = 3;
        this.add(periodSummary_Table_panel, summaryContainer_C);

        JTabbedPane master = new JTabbedPane();
        JPanel bank = new JPanel(new BorderLayout());
        JPanel fund = new JPanel(new BorderLayout());

        if (bankSummary_panel != null) {
            bank.add(bankSummary_panel, BorderLayout.WEST);
        }
        bank.add(bankCategoryTransfer_panel, BorderLayout.CENTER);

        fund.add(fundEventSummary_panel, BorderLayout.WEST);
        fund.add(periodFundTransfer_panel, BorderLayout.CENTER);

        master.addTab("Bank", bank);
        master.addTab("Fund", fund);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;
        this.add(master, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodFundTransfer_panel.update();

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
            List<?> selected = bankSummary_panel.getMainPanel().getSelectedItems();
            if (selected.size() == 1) {
                selectedBank = ((Bank_Summary) selected.get(0)).getPool();
            }
        }

        // Update children on the selection
        bankCategoryTransfer_set.setSecondaryParent(selectedBank);
        bankCategoryTransfer_controller.setBank(selectedBank);

        // Update the UI
        periodSummary_Table_panel.update();
        bankCategoryTransfer_panel.update();
    }
}
