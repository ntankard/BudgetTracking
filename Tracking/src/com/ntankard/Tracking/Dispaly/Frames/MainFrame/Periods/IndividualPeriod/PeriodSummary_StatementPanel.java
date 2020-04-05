package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.SetFilter;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
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

import static com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Model.CustomFormatter;
import static java.awt.GridBagConstraints.BOTH;

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

    private ManualBankTransfer_ElementController bankCategoryTransferAll_controller;
    private TwoParent_Children_Set<BankTransfer, Period, Bank> bankCategoryTransferAll_set;
    private DataObject_DisplayList<BankTransfer> bankCategoryTransferAll_panel;

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

        // Formatter ---------------------------------------------------------------------------------------------------

        CustomFormatter bankTransferFormatter = (dataObject, rendererObject) -> {
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
        };

        CustomFormatter bankBankTransferFormatter = (dataObject, rendererObject) -> {
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
        };

        // Category table ----------------------------------------------------------------------------------------------

        categorySummary_table = new PeriodSummary_Table<>(period, true, Category.class, this);
        categorySummary_table.getModel().addCustomFormatter(bankTransferFormatter);


        // Bank table --------------------------------------------------------------------------------------------------

        bankSummary_table = new PeriodSummary_Table<>(period, false, Bank.class, this);
        bankSummary_table.getModel().addCustomFormatter(bankBankTransferFormatter);

        // Fund Event table --------------------------------------------------------------------------------------------

        fundEventSummary_table = new PeriodSummary_Table<>(period, false, FundEvent.class, this);
        fundEventSummary_table.getModel().addCustomFormatter(bankTransferFormatter);

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

        bankCategoryTransfer_set = new TwoParent_Children_Set<>(BankTransfer.class, period, null, new SetFilter<BankTransfer>(null) {
            @Override
            protected boolean shouldAdd_Impl(BankTransfer dataObject) {
                if (selectedBank != null) {
                    if ((dataObject.getSource().equals(selectedBank) && dataObject.getPeriod().equals(period))) {
                        return true;
                    }
                    if ((dataObject.getDestination().equals(selectedBank))) {
                        if (dataObject.getDestinationPeriod() != null) {
                            return dataObject.getDestinationPeriod().equals(period);
                        } else {
                            return dataObject.getPeriod().equals(period);
                        }
                    }
                }
                return false;
            }
        });
        bankCategoryTransfer_controller = new ManualBankTransfer_ElementController(period, this);
        bankCategoryTransfer_panel = new DataObject_DisplayList<>(BankTransfer.class, bankCategoryTransfer_set, false, this);
        bankCategoryTransfer_panel.addControlButtons(bankCategoryTransfer_controller);

        bankCategoryTransferAll_set = new TwoParent_Children_Set<>(BankTransfer.class, period, null);
        bankCategoryTransferAll_controller = new ManualBankTransfer_ElementController(period, this);
        bankCategoryTransferAll_panel = new DataObject_DisplayList<>(BankTransfer.class, bankCategoryTransferAll_set, false, this);
        bankCategoryTransferAll_panel.addControlButtons(bankCategoryTransferAll_controller);

        // Main layout -------------------------------------------------------------------------------------------------

        GridBagConstraints masterContainer_C = new GridBagConstraints();
        masterContainer_C.fill = BOTH;
        masterContainer_C.weightx = 1;

        masterContainer_C.weighty = 2;
        masterContainer_C.gridwidth = 3;
        this.add(table_scroll, masterContainer_C);

        JPanel bankPanel = new JPanel(new GridBagLayout());
        JPanel fundPanel = new JPanel(new BorderLayout());

        GridBagConstraints bankPanel_C = new GridBagConstraints();
        bankPanel_C.weightx = 1;
        bankPanel_C.weighty = 1;
        bankPanel_C.fill = BOTH;


        bankPanel_C.gridx = 1;
        if (bankSummary_panel != null) {
            bankPanel.add(bankSummary_panel, bankPanel_C);
        }
        bankPanel_C.gridx = 2;
        JTabbedPane bankTrans = new JTabbedPane();
        bankTrans.addTab("All", bankCategoryTransferAll_panel);
        bankTrans.addTab("Me", bankCategoryTransfer_panel);
        bankPanel.add(bankTrans, bankPanel_C);

        fundPanel.add(fundEventSummary_panel, BorderLayout.WEST);
        fundPanel.add(periodFundTransfer_panel, BorderLayout.CENTER);

        JTabbedPane transferPanel = new JTabbedPane();
        transferPanel.addTab("Bank", bankPanel);
        transferPanel.addTab("Fund", fundPanel);

        masterContainer_C.weighty = 1;
        masterContainer_C.gridwidth = 1;
        masterContainer_C.gridy = 1;
        this.add(transferPanel, masterContainer_C);
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
        bankCategoryTransferAll_set.setSecondaryParent(selectedBank);
        bankCategoryTransferAll_controller.setBank(selectedBank);

        // Update the UI
        categorySummary_table.update();
        bankSummary_table.update();
        fundEventSummary_table.update();
        bankCategoryTransfer_panel.update();
        bankCategoryTransferAll_panel.update();
    }
}
