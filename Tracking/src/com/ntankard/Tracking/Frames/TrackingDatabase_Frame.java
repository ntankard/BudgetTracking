package com.ntankard.Tracking.Frames;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.*;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Interface.PeriodCategory;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class TrackingDatabase_Frame extends JPanel implements Updatable {

    // Core Data
    private TrackingDatabase trackingDatabase;

    // The data displayed (clone of the data in the database)
    private List<Currency> currency_list = new ArrayList<>();
    private List<Category> category_list = new ArrayList<>();
    private List<Bank> bank_list = new ArrayList<>();
    private List<Period> period_list = new ArrayList<>();
    private List<Statement> statement_list = new ArrayList<>();
    private List<Transaction> transaction_list = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<PeriodCategory> periodCategory_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Currency> currency_panel;
    private DynamicGUI_DisplayList<Category> category_panel;
    private DynamicGUI_DisplayList<Bank> bank_panel;
    private DynamicGUI_DisplayList<Period> period_panel;
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private static DynamicGUI_DisplayList.ListControl_Button managePeriod;
    private DynamicGUI_DisplayList<PeriodCategory> periodCategory_table;
    private JButton upload_btn;
    private JTabbedPane structures_tPanel;

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

            //managePeriod.doClick();
        });
    }

    /**
     * Constructor
     *
     * @param trackingDatabase The data use to populate the frame
     */
    private TrackingDatabase_Frame(TrackingDatabase trackingDatabase) {
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
        update();
    }


    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1500, 1000));

        upload_btn = new JButton("Save");
        upload_btn.addActionListener(e -> TrackingDatabase_Reader.save(trackingDatabase, "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData"));
        this.add(upload_btn, BorderLayout.NORTH);

        period_panel = DynamicGUI_DisplayList.newIntractableTable(period_list, new MemberClass(Period.class), true, ALWAYS_DISPLAY, this);
        currency_panel = DynamicGUI_DisplayList.newIntractableTable(currency_list, new MemberClass(Currency.class), true, ALWAYS_DISPLAY, this);
        category_panel = DynamicGUI_DisplayList.newIntractableTable(category_list, new MemberClass(Category.class), true, ALWAYS_DISPLAY, this);
        bank_panel = DynamicGUI_DisplayList.newIntractableTable(bank_list, new MemberClass(Bank.class), true, ALWAYS_DISPLAY, this);
        statement_panel = DynamicGUI_DisplayList.newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);
        transaction_panel = DynamicGUI_DisplayList.newIntractableTable(transaction_list, new MemberClass(Transaction.class), true, ALWAYS_DISPLAY, this);
        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), true, ALWAYS_DISPLAY, this);
        periodCategory_table = DynamicGUI_DisplayList.newIntractableTable(periodCategory_list, new MemberClass(PeriodCategory.class), false, ALWAYS_DISPLAY, this);

        transaction_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Transaction transaction = (Transaction)rowObject;
            if(transaction.getIdStatement().getIdBank().getCurrency().getId().equals("YEN")){
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        statement_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Statement statement = (Statement)rowObject;
            if(statement.getIdBank().getCurrency().getId().equals("YEN")){
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        managePeriod = new DynamicGUI_DisplayList.ListControl_Button<>("Manage Period", period_panel, SINGLE, false);//new SetRecord()
        managePeriod.addActionListener(e -> {
            List selected = period_panel.getMainPanel().getSelectedItems();
            Period_Frame.open(trackingDatabase, (Period) selected.get(0), this);
        });
        period_panel.addButton(managePeriod);

        structures_tPanel = new JTabbedPane();
        structures_tPanel.addTab("Period", period_panel);
        structures_tPanel.addTab("Category Total", periodCategory_table);
        structures_tPanel.addTab("Transaction", transaction_panel);
        structures_tPanel.addTab("Currency", currency_panel);
        structures_tPanel.addTab("Category", category_panel);
        structures_tPanel.addTab("Bank", bank_panel);
        structures_tPanel.addTab("Statement", statement_panel);
        structures_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        this.add(structures_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyUpdate() {
        SwingUtilities.invokeLater(this::update);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        currency_list.clear();
        category_list.clear();
        bank_list.clear();
        period_list.clear();
        statement_list.clear();
        transaction_list.clear();
        categoryTransfer_list.clear();
        periodCategory_list.clear();

        currency_list.addAll(trackingDatabase.getCurrencies());
        category_list.addAll(trackingDatabase.getCategories());
        bank_list.addAll(trackingDatabase.getBanks());
        period_list.addAll(trackingDatabase.getPeriods());
        statement_list.addAll(trackingDatabase.getStatements());
        transaction_list.addAll(trackingDatabase.getTransactions());
        categoryTransfer_list.addAll(trackingDatabase.getCategoryTransfer());
        periodCategory_list.addAll(trackingDatabase.getPeriodCategory());

        currency_panel.update();
        category_panel.update();
        bank_panel.update();
        period_panel.update();
        statement_panel.update();
        transaction_panel.update();
        categoryTransfer_panel.update();
        periodCategory_table.update();

        //period_panel.getMainPanel().getListSelectionModel().setSelectionInterval(0, 0);
    }
}
