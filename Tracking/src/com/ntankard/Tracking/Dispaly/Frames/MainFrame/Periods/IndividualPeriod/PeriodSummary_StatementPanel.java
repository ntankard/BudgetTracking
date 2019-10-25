package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Frames.Statement_Frame;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.CurrencyBound_LocaleSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class PeriodSummary_StatementPanel extends UpdatableJPanel {

    // Core Data
    private Period core;
    private Statement selectedStatement = null;

    // The data displayed (clone of the data in the database)
    private List<Transaction> transaction_list = new ArrayList<>();
    private List<Statement> statement_list = new ArrayList<>();

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private DynamicGUI_IntractableObject period_panel;
    private DynamicGUI_IntractableObject periodTotal_panel;

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

        statement_panel = new DynamicGUI_DisplayList<>(statement_list, new MemberClass(Statement.class), this);
        statement_panel.getMainPanel().setLocaleSource(new CurrencyBound_LocaleSource());

        DynamicGUI_DisplayList.ListControl_Button manageStatementBtn = new DynamicGUI_DisplayList.ListControl_Button<Statement>("Manage Statement", statement_panel, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open((Statement) selected.get(0), this);
        });
        statement_panel.addButton(manageStatementBtn);
        statement_panel.getMainPanel().getListSelectionModel().addListSelectionListener(e -> updateTransactions());

        transaction_panel = new DynamicGUI_DisplayList<>(transaction_list, new MemberClass(Transaction.class), this)
                .setLocaleSource(new CurrencyBound_LocaleSource())
                .setSources(TrackingDatabase.get())
                .addControlButtons(new DynamicGUI_DisplayList.ElementController<Transaction>() {
                    @Override
                    public Transaction newElement() {
                        String idCode = TrackingDatabase.get().getNextId(Transaction.class);
                        return new Transaction(selectedStatement, idCode, "", 0.0, TrackingDatabase.get().get(Category.class, "Unaccounted"));
                    }

                    @Override
                    public void deleteElement(Transaction toDel) {
                        TrackingDatabase.get().remove(toDel);
                        notifyUpdate();
                    }

                    @Override
                    public void addElement(Transaction newObj) {
                        TrackingDatabase.get().add(newObj);
                        notifyUpdate();
                    }
                });
        transaction_panel.getMainPanel().setLocaleSource(new CurrencyBound_LocaleSource());

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
        statement_list.clear();
        statement_list.addAll(core.getChildren(Statement.class));
        statement_list.sort((o1, o2) -> {
            if (o1.getIdBank().getOrder() == o2.getIdBank().getOrder()) {
                return 0;
            } else if (o1.getIdBank().getOrder() > o2.getIdBank().getOrder()) {
                return 1;
            }
            return -1;
        });
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

        // Populate the list from the selected statement
        transaction_list.clear();
        if (selectedStatement != null) {
            transaction_list.addAll(selectedStatement.getChildren(Transaction.class));
        }

        // Update the UI
        transaction_panel.update();
        periodSummary_panel.update();
        period_panel.update();
        periodTotal_panel.update();
    }
}
