package com.ntankard.Tracking.Frames;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.Object.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Statement;
import com.ntankard.Tracking.DataBase.Core.Transaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class Period_Frame extends UpdatableJPanel {

    // Core Data
    private TrackingDatabase trackingDatabase;
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<Statement> statement_list = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<Transaction> transaction_list = new ArrayList<>();

    // The GUI components
    private static DynamicGUI_DisplayList.ListControl_Button setRecord;
    private DynamicGUI_IntractableObject period_panel;
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private PeriodSummary periodSummary_panel;

    /**
     * Create and open the period frame
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     * @param master           The parent of this frame
     */
    public static void open(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        JFrame _frame = new JFrame("Period");
        _frame.setContentPane(new Period_Frame(trackingDatabase, core, master));
        _frame.pack();
        _frame.setVisible(true);
        _frame.setLocation(0, 50);

        _frame.repaint();

        //setRecord.doClick();
    }

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     * @param master           The parent of this frame
     */
    private Period_Frame(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
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

        statement_panel = DynamicGUI_DisplayList.newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);
        statement_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Statement statement = (Statement) rowObject;
            if (statement.getIdBank().getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        setRecord = new DynamicGUI_DisplayList.ListControl_Button<>("Manage Period", statement_panel, SINGLE, false);
        setRecord.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open(trackingDatabase, (Statement) selected.get(0), this);
        });
        statement_panel.addButton(setRecord);

        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<CategoryTransfer>() {
            @Override
            public void deleteElement(CategoryTransfer toDel) {
                trackingDatabase.removeCategoryTransfer(toDel);
                notifyUpdate();
            }

            @Override
            public CategoryTransfer newElement() {
                String idCode = trackingDatabase.getNextCategoryTransferId(core);
                return new CategoryTransfer(core, idCode, trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCurrency("YEN"), "", 0.0);
            }

            @Override
            public void addElement(CategoryTransfer newObj) {
                trackingDatabase.addCategoryTransfer(newObj);
                notifyUpdate();
            }
        }, this, trackingDatabase);

        transaction_panel = DynamicGUI_DisplayList.newIntractableTable(transaction_list, new MemberClass(Transaction.class), true, ALWAYS_DISPLAY, this);
        transaction_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Transaction transaction = (Transaction) rowObject;
            if (transaction.getIdStatement().getIdBank().getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        periodSummary_panel = new PeriodSummary(trackingDatabase, core, this);

        this.add(statement_panel, BorderLayout.CENTER);

        JTabbedPane data_tPanel = new JTabbedPane();
        data_tPanel.addTab("Summary", periodSummary_panel);
        data_tPanel.addTab("Statement", statement_panel);
        data_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        data_tPanel.addTab("Transactions", transaction_panel);
        this.add(data_tPanel, BorderLayout.CENTER);

        period_panel = DynamicGUI_IntractableObject.newIntractableObjectPanel(core, INFO_DISPLAY, false, this, trackingDatabase);
        this.add(period_panel, BorderLayout.EAST);
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
        statement_list.clear();
        categoryTransfer_list.clear();
        transaction_list.clear();

        statement_list.addAll(core.getStatements());
        categoryTransfer_list.addAll(core.getCategoryTransfers());
        transaction_list.addAll(core.getTransactions());

        statement_panel.update();
        categoryTransfer_panel.update();
        transaction_panel.update();

        periodSummary_panel.update();

        //statement_panel.getMainPanel().getListSelectionModel().setSelectionInterval(4, 4);
    }
}
