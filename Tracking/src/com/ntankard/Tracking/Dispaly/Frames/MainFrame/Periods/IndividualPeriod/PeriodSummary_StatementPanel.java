package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
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
    private PeriodSummary_Table<Category> categorySummary_table;
    private PeriodSummary_Table<Bank> bankSummary_table;
    private PeriodSummary_Table<FundEvent> fundEventSummary_table;

    private Object_DisplayList<Bank_Summary> bankSummary_panel;
    private Object_DisplayList<FundEvent_Summary> fundEventSummary_panel;

    private ManualBankTransfer_ElementController bankCategoryTransfer_controller;
    private TwoParent_Children_Set<BankTransfer, Period, Bank> bankCategoryTransfer_set;
    private DataObject_DisplayList<BankTransfer> bankCategoryTransfer_panel;

    private DataObject_DisplayList<ManualFundTransfer> periodFundTransfer_panel;

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

        // Category table ----------------------------------------------------------------------------------------------

        categorySummary_table = new PeriodSummary_Table<>(period, true, Category.class, this);
        categorySummary_table.getModel().addCustomFormatter((dataObject, rendererObject) -> {

            if (dataObject instanceof HalfTransfer) {
                HalfTransfer halfTransfer = (HalfTransfer) dataObject;
                if (halfTransfer.getTransfer() instanceof BankTransfer) {

                    // Check if its selected
                    boolean selected = false;
                    BankTransfer transaction = (BankTransfer) halfTransfer.getTransfer();
                    if (selectedBank != null) {
                        selected = transaction.getSource().equals(selectedBank);
                    }

                    if (!halfTransfer.getTransfer().getSourceTransfer().getPeriod().equals(period)) {
                        rendererObject.background = new Color(230, 204, 255);
                    } else if (halfTransfer.getTransfer() instanceof RecurringBankTransfer) {
                        if (halfTransfer.getTransfer().getChildren(Receipt.class).size() != 0) {
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
                        if (halfTransfer.getTransfer().getChildren(Receipt.class).size() != 0) {
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
                } else if (halfTransfer.getTransfer() instanceof FundTransfer) {
                    if (halfTransfer.getTransfer() instanceof RePayFundTransfer) {
                        rendererObject.background = new Color(204, 238, 255);
                    }
                }
            }
        });

        // Bank table --------------------------------------------------------------------------------------------------

        bankSummary_table = new PeriodSummary_Table<>(period, false, Bank.class, this);
        bankSummary_table.getModel().addCustomFormatter((dataObject, rendererObject) -> {
            if (dataObject instanceof HalfTransfer) {
                HalfTransfer halfTransfer = (HalfTransfer) dataObject;
                BankTransfer transaction = (BankTransfer) halfTransfer.getTransfer();

                boolean amSource = halfTransfer.equals(transaction.getSourceTransfer());
                boolean sourceSelected = false;
                boolean destinationSelected = false;
                if (selectedBank != null) {
                    sourceSelected = transaction.getSourceTransfer().getPool().equals(selectedBank);
                }
                if (selectedBank != null) {
                    destinationSelected = transaction.getDestinationTransfer().getPool().equals(selectedBank);
                }


                if (transaction.getDestinationPeriod() != null) {
                    rendererObject.background = new Color(230, 204, 255);
                } else {
                    if (amSource && sourceSelected || !amSource && destinationSelected) {
                        rendererObject.background = new Color(255, 255, 0);
                    } else if (sourceSelected || destinationSelected) {
                        rendererObject.background = new Color(255, 102, 0);
                    }
                }
            }
        });

        // Fund Event table --------------------------------------------------------------------------------------------

        fundEventSummary_table = new PeriodSummary_Table<>(period, false, FundEvent.class, this);
        fundEventSummary_table.getModel().addCustomFormatter((dataObject, rendererObject) -> {
            if (dataObject instanceof HalfTransfer) {
                HalfTransfer halfTransfer = (HalfTransfer) dataObject;
            }
        });

        // Table combination -------------------------------------------------------------------------------------------

        JScrollPane table_scroll = new JScrollPane();
        JPanel full = new JPanel(new GridBagLayout());

        GridBagConstraints table_scroll_C = new GridBagConstraints();
        table_scroll_C.anchor = GridBagConstraints.WEST;
        table_scroll_C.weightx = 1;
        table_scroll_C.weighty = 1;

        full.add(categorySummary_table, table_scroll_C);
        table_scroll_C.gridy = 1;
        full.add(bankSummary_table, table_scroll_C);
        table_scroll_C.gridy = 2;
        full.add(fundEventSummary_table, table_scroll_C);

        table_scroll.setViewportView(full);
        table_scroll.getVerticalScrollBar().setUnitIncrement(12);
        table_scroll.getHorizontalScrollBar().setUnitIncrement(12);

        // Transfers ---------------------------------------------------------------------------------------------------

        periodFundTransfer_panel = new DataObject_DisplayList<>(ManualFundTransfer.class, new OneParent_Children_Set<>(ManualFundTransfer.class, period), false, this);
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
        this.add(table_scroll, summaryContainer_C);

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
        categorySummary_table.update();
        bankSummary_table.update();
        fundEventSummary_table.update();
        bankCategoryTransfer_panel.update();
    }
}
