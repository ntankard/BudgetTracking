package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Frames.Statement_Frame;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.MoneyEventLocaleInspector;
import com.ntankard.Tracking.Dispaly.Util.StatementLocaleInspector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.*;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.newIntractableTable;

public class PeriodSummary_StatementPanel extends UpdatableJPanel implements ListSelectionListener {

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

        statement_panel = newIntractableTable(statement_list, new MemberClass(Statement.class), false, ALWAYS_DISPLAY, this);
        statement_panel.getMainPanel().setLocaleInspector(new StatementLocaleInspector());

        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", statement_panel, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open((Statement) selected.get(0), this);
        });
        statement_panel.addButton(manageStatementBtn);
        statement_panel.getMainPanel().getListSelectionModel().addListSelectionListener(this);

        transaction_panel = newIntractableTable(transaction_list, new MemberClass(Transaction.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<Transaction>() {
            @Override
            public Transaction newElement() {
                String idCode = TrackingDatabase.get().getNextTransactionId(selectedStatement);
                return new Transaction(selectedStatement, idCode, "", 0.0, TrackingDatabase.get().getCategory("Unaccounted"));
            }

            @Override
            public void deleteElement(Transaction toDel) {
                TrackingDatabase.get().removeTransaction(toDel);
                notifyUpdate();
            }

            @Override
            public void addElement(Transaction newObj) {
                TrackingDatabase.get().addTransaction(newObj);
                notifyUpdate();
            }
        }, this, TrackingDatabase.get());
        transaction_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 2;
        this.add(periodSummary_panel, summaryContainer_C);

        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        this.add(statement_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
        this.add(transaction_panel, summaryContainer_C);
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
        statement_panel.update();
        statement_panel.getMainPanel().getListSelectionModel().setSelectionInterval(min, max);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        updateTransactions();
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
    }
}
