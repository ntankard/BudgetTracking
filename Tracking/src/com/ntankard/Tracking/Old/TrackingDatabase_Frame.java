package com.ntankard.Tracking.Old;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

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
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();
    private List<Fund> fund_list = new ArrayList<>();
    private List<PeriodFundTransfer> periodFundTransfer_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Currency> currency_panel;
    private DynamicGUI_DisplayList<Category> category_panel;
    private DynamicGUI_DisplayList<Bank> bank_panel;
    private DynamicGUI_DisplayList<Period> period_panel;
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private static DynamicGUI_DisplayList.ListControl_Button managePeriod;
    private DynamicGUI_DisplayList.ListControl_Button newPeriod;
    private DynamicGUI_DisplayList<PeriodCategory> periodCategory_table;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DynamicGUI_DisplayList<Fund> fund_panel;
    private DynamicGUI_DisplayList<PeriodFundTransfer> periodFundTransfer_panel;
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
        periodTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodTransfer_list, new MemberClass(PeriodTransfer.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<PeriodTransfer>() {
            @Override
            public PeriodTransfer newElement() {
                String idCode = trackingDatabase.getNextId(PeriodTransfer.class);
                return new PeriodTransfer(idCode, trackingDatabase.<Period>get(Period.class).get(0), trackingDatabase.<Period>get(Period.class).get(1), trackingDatabase.get(Currency.class, "YEN"), trackingDatabase.get(Category.class, "Unaccounted"), "", 0.0);
            }

            @Override
            public void deleteElement(PeriodTransfer toDel) {
                trackingDatabase.remove(toDel);
                notifyUpdate();
            }

            @Override
            public void addElement(PeriodTransfer newObj) {
                trackingDatabase.add(newObj);
                notifyUpdate();
            }
        }, null, this, trackingDatabase);
        fund_panel = DynamicGUI_DisplayList.newIntractableTable(fund_list, new MemberClass(Fund.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<Fund>() {
            @Override
            public Fund newElement() {
                return new Fund("");
            }

            @Override
            public void deleteElement(Fund toDel) {

            }

            @Override
            public void addElement(Fund newObj) {
                trackingDatabase.add(newObj);
                notifyUpdate();
            }
        }, null, this, trackingDatabase);
        periodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodFundTransfer_list, new MemberClass(PeriodFundTransfer.class), true, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<PeriodFundTransfer>() {
            @Override
            public PeriodFundTransfer newElement() {
                String idCode = trackingDatabase.getNextId(PeriodFundTransfer.class);
                return new PeriodFundTransfer(idCode, trackingDatabase.<Period>get(Period.class).get(0), trackingDatabase.get(Fund.class).get(0), trackingDatabase.get(Category.class, "Unaccounted"), trackingDatabase.get(Fund.class).get(0).<FundEvent>getChildren(FundEvent.class).get(0), trackingDatabase.get(Currency.class, "YEN"), "", 0.0);
            }

            @Override
            public void deleteElement(PeriodFundTransfer toDel) {

            }

            @Override
            public void addElement(PeriodFundTransfer newObj) {
                trackingDatabase.add(newObj);
                notifyUpdate();
            }
        }, null, this, trackingDatabase);

        transaction_panel.getMainPanel().setNumberFormatSource(rowObject -> {
            Transaction transaction = (Transaction) rowObject;
            return transaction.getSourceContainer().getIdBank().getCurrency().getNumberFormat();
        });
        statement_panel.getMainPanel().setNumberFormatSource(rowObject -> {
            Statement statement = (Statement) rowObject;
            return statement.getIdBank().getCurrency().getNumberFormat();
        });

        managePeriod = new DynamicGUI_DisplayList.ListControl_Button<>("Manage Period", period_panel, SINGLE, false);
        managePeriod.addActionListener(e -> {
            List selected = period_panel.getMainPanel().getSelectedItems();
            Period_Frame.open(trackingDatabase, (Period) selected.get(0), this);
        });
        period_panel.addButton(managePeriod);

        newPeriod = new DynamicGUI_DisplayList.ListControl_Button<>("New Period", period_panel);
        newPeriod.addActionListener(e -> {
            Period last = trackingDatabase.<Period>get(Period.class).get(trackingDatabase.<Period>get(Period.class).size() - 1);
            Period period = Period.Month(last.getNextPeriodTime().get(Calendar.MONTH) + 1, last.getNextPeriodTime().get(Calendar.YEAR));
            trackingDatabase.add(period);
            notifyUpdate();
        });
        period_panel.addButton(newPeriod);

        structures_tPanel = new JTabbedPane();
        structures_tPanel.addTab("Period", period_panel);
        structures_tPanel.addTab("Period Transfer", periodTransfer_panel);
        structures_tPanel.addTab("Category Total", periodCategory_table);
        structures_tPanel.addTab("Transaction", transaction_panel);
        structures_tPanel.addTab("Currency", currency_panel);
        structures_tPanel.addTab("Category", category_panel);
        structures_tPanel.addTab("Bank", bank_panel);
        structures_tPanel.addTab("Statement", statement_panel);
        structures_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        structures_tPanel.addTab("Non Period Fund", fund_panel);
        structures_tPanel.addTab("Non Period Fund Transfer", periodFundTransfer_panel);
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
        periodTransfer_list.clear();
        fund_list.clear();
        periodFundTransfer_list.clear();

        currency_list.addAll(trackingDatabase.get(Currency.class));
        category_list.addAll(trackingDatabase.get(Category.class));
        bank_list.addAll(trackingDatabase.get(Bank.class));
        period_list.addAll(trackingDatabase.get(Period.class));
        statement_list.addAll(trackingDatabase.get(Statement.class));
        transaction_list.addAll(trackingDatabase.get(Transaction.class));
        categoryTransfer_list.addAll(trackingDatabase.get(CategoryTransfer.class));
        //periodCategory_list.addAll(trackingDatabase.get(PeriodTransfer.class));
        periodTransfer_list.addAll(trackingDatabase.get(PeriodTransfer.class));
        fund_list.addAll(trackingDatabase.get(Fund.class));
        periodFundTransfer_list.addAll(trackingDatabase.get(PeriodFundTransfer.class));

        currency_panel.update();
        category_panel.update();
        bank_panel.update();
        period_panel.update();
        statement_panel.update();
        transaction_panel.update();
        categoryTransfer_panel.update();
        periodCategory_table.update();
        periodTransfer_panel.update();
        fund_panel.update();
        periodFundTransfer_panel.update();

        //period_panel.getMainPanel().getListSelectionModel().setSelectionInterval(0, 0);
    }
}
