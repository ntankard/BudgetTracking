package com.ntankard.Tracking.Dispaly.Frames;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.Object.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class Statement_Frame extends UpdatableJPanel {

    // Core Data
    private Statement core;

    // The data displayed (clone of the data in the database)
    private List<Transaction> transaction_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private DynamicGUI_IntractableObject statement_panel;

    /**
     * Create and open the period frame
     *
     * @param core   The Statement this panel is built around
     * @param master The parent of this frame
     */
    public static void open(Statement core, Updatable master) {
        JFrame _frame = new JFrame("Statement");
        _frame.setContentPane(new Statement_Frame(core, master));
        _frame.pack();
        _frame.setVisible(true);

        _frame.repaint();
        _frame.setLocation(50, 100);
        _frame.toFront();
    }

    /**
     * Constructor
     *
     * @param core   The Statement this panel is built around
     * @param master The parent of this frame
     */
    private Statement_Frame(Statement core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setPreferredSize(new Dimension(1800, 800));
        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());

        transaction_panel = DynamicGUI_DisplayList.newIntractableTable(transaction_list, new MemberClass(Transaction.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<Transaction>() {

            @Override
            public Transaction newElement() {
                String idCode = TrackingDatabase.get().getNextTransactionId(core);
                return new Transaction(core, idCode, "", 0.0, TrackingDatabase.get().getCategory("Unaccounted"));
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
        transaction_panel.getMainPanel().setLocaleInspector(rowObject -> {
            if (core.getIdBank().getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        JTabbedPane data_tPanel = new JTabbedPane();
        data_tPanel.addTab("Transactions", transaction_panel);
        this.add(data_tPanel, BorderLayout.CENTER);

        statement_panel = DynamicGUI_IntractableObject.newIntractableObjectPanel(core, INFO_DISPLAY, false, this, TrackingDatabase.get());
        this.add(statement_panel, BorderLayout.EAST);
    }

    /**
     * Short circuit to prevent the top level panel getting effected
     */
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
        SwingUtilities.invokeLater(this::update);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        transaction_list.clear();

        transaction_list.addAll(core.<Transaction>getChildren(Transaction.class));

        transaction_panel.update();
        statement_panel.update();
    }
}