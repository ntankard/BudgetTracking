package com.ntankard.Tracking.Frames;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.*;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class TrackingDatabase_Frame extends JPanel implements Updatable {

    // Core Data
    private TrackingDatabase trackingDatabase;

    // The data displayed (clone of the data in the database)
    private List<Currency> currency_list = new ArrayList<>();
    private List<Bank> bank_list = new ArrayList<>();
    private List<Period> period_list = new ArrayList<>();
    private List<Statement> statement_list = new ArrayList<>();
    private List<Transaction> transaction_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList currency_panel;
    private DynamicGUI_DisplayList bank_panel;
    private DynamicGUI_DisplayList period_panel;
    private DynamicGUI_DisplayList statement_panel;
    private DynamicGUI_DisplayList transaction_panel;

    /**
     * Create and open the tracking frame
     *
     * @param trackingDatabase The data use to populate the frame
     */
    public static void open(TrackingDatabase trackingDatabase) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new TrackingDatabase_Frame(trackingDatabase));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     *
     * @param trackingDatabase The data use to populate the frame
     */
    public TrackingDatabase_Frame(TrackingDatabase trackingDatabase) {
        this.trackingDatabase = trackingDatabase;
        this.setPreferredSize(new Dimension(1500, 1000));
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        currency_panel      = DynamicGUI_DisplayList.newIntractableTable(currency_list,     new MemberClass(Currency.class),    true, ALWAYS_DISPLAY,this);
        bank_panel          = DynamicGUI_DisplayList.newIntractableTable(bank_list,         new MemberClass(Bank.class),        true, ALWAYS_DISPLAY,this);
        period_panel        = DynamicGUI_DisplayList.newIntractableTable(period_list,       new MemberClass(Period.class),      true, ALWAYS_DISPLAY,this);
        statement_panel     = DynamicGUI_DisplayList.newIntractableTable(statement_list,    new MemberClass(Statement.class),   true, ALWAYS_DISPLAY,this);
        transaction_panel   = DynamicGUI_DisplayList.newIntractableTable(transaction_list,  new MemberClass(Transaction.class), true, true, ALWAYS_DISPLAY,this, trackingDatabase);

        JTabbedPane structures_tPanel = new JTabbedPane();

        structures_tPanel.addTab("Transaction", transaction_panel);
        structures_tPanel.addTab("Currency",    currency_panel);
        structures_tPanel.addTab("Bank",        bank_panel);
        structures_tPanel.addTab("Period",      period_panel);
        structures_tPanel.addTab("Statement",   statement_panel);


        GridConstraints structures_tPanel_C = new GridConstraints(
                1, 0, 1, 2,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, new Dimension(200, 200), null);

        this.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        //this.add(autoCorrelate_btn, autoCorrelate_btn_C);
        //this.add(upload_btn, upload_btn_C);
        this.add(structures_tPanel, structures_tPanel_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyUpdate() {
        SwingUtilities.invokeLater(() -> update());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        currency_list.clear();
        bank_list.clear();
        period_list.clear();
        statement_list.clear();
        transaction_list.clear();

        currency_list.addAll(trackingDatabase.getCurrencies());
        bank_list.addAll(trackingDatabase.getBanks());
        period_list.addAll(trackingDatabase.getPeriods());
        statement_list.addAll(trackingDatabase.getStatements());
        transaction_list.addAll(trackingDatabase.getTransactions());

        currency_panel.update();
        bank_panel.update();
        period_panel.update();
        statement_panel.update();
        transaction_panel.update();
    }
}