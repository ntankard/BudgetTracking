package com.ntankard.budgetTracking.display.frames.mainFrame;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.display.frames.mainFrame.funds.FundPanel;
import com.ntankard.budgetTracking.display.frames.mainFrame.image.ReceiptPanel;
import com.ntankard.budgetTracking.display.frames.mainFrame.periods.PeriodTabPanel;
import com.ntankard.budgetTracking.display.frames.mainFrame.statement.StatementPanel;
import com.ntankard.budgetTracking.display.frames.mainFrame.summaryGraphs.SummaryGraphPanel;
import com.ntankard.dynamicGUI.gui.util.containers.ButtonPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.io.Database_IO;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

public class Master_Frame extends JPanel implements Updatable {

    // The GUI components
    private PeriodTabPanel periodPanel;
    private FundPanel fundPanel;
    private DatabasePanel databasePanel;
    private SummaryGraphPanel summaryGraphPanel;
    private SummaryPanel summaryPanel;
    private ReceiptPanel receiptPanel;
    private RecurringPaymentPanel recurringPaymentPanel;
    private CategorySetPanel categorySetPanel;
    private BankSummaryPanel bankSummaryPanel;
    private StatementPanel statementPanel;

    private final String savePath;

    // Core database
    private final Database database;

    /**
     * Create and open the tracking frame
     */
    public static void open(Database database, String savePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new Master_Frame(database, savePath));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     */
    private Master_Frame(Database database, String savePath) {
        this.database = database;
        this.savePath = savePath;
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

        JButton save_btn = new JButton("Save");
        save_btn.addActionListener(e -> {
            Database_IO.save(database, savePath);
        });

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        JButton addPeriod_btn = new JButton("Add Period");
        addPeriod_btn.addActionListener(e -> {
            ExistingPeriod last = database.get(ExistingPeriod.class).get(database.get(ExistingPeriod.class).size() - 1);
            Period period = last.generateNext();
            period.add();
            notifyUpdate();
        });

        JButton printRemainingTransactions_btn = new JButton("Print unknown");
        printRemainingTransactions_btn.addActionListener(e -> {
            printRemainingTransactions();
        });

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(update_btn);
        btnPanel.addButton(addPeriod_btn);
        btnPanel.addButton(save_btn);
        btnPanel.addButton(printRemainingTransactions_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodTabPanel(database, this);
        fundPanel = new FundPanel(database, this);
        databasePanel = new DatabasePanel(database, this);
        summaryGraphPanel = new SummaryGraphPanel(database, this);
        summaryPanel = new SummaryPanel(database, this);
        receiptPanel = new ReceiptPanel(database, this);
        recurringPaymentPanel = new RecurringPaymentPanel(database, this);
        categorySetPanel = new CategorySetPanel(database, this);
        bankSummaryPanel = new BankSummaryPanel(database, this);
        statementPanel = new StatementPanel(database, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Periods", periodPanel);
        master_tPanel.addTab("Fund", fundPanel);
        master_tPanel.addTab("Database", databasePanel);
        master_tPanel.addTab("Summary Graphs", summaryGraphPanel);
        master_tPanel.addTab("Summary", summaryPanel);
        master_tPanel.addTab("Receipts", receiptPanel);
        master_tPanel.addTab("Recurring Payments", recurringPaymentPanel);
        master_tPanel.addTab("Category Set", categorySetPanel);
        master_tPanel.addTab("Banks", bankSummaryPanel);
        master_tPanel.addTab("Statement", statementPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * Print all unknown transaction to find the needed translations
     */
    private void printRemainingTransactions() {
        // For each bank
        for (Bank bank : database.get(Bank.class)) {

            // Get all unbound statement transactions
            List<StatementTransaction> unlinked = new OneParent_Children_Set<>(StatementTransaction.class, bank, new SetFilter<StatementTransaction>() {
                @Override
                protected boolean shouldAdd_Impl(StatementTransaction dataObject) {
                    return dataObject.getStatementBankTransfer() == null;
                }
            }).get();

            if (unlinked.size() == 0) {
                continue;
            }

            System.out.println(bank.toString());

            Double total = 0.0;
            Map<String, StatementTransaction> names = new HashMap<>();
            Map<String, Double> totals = new HashMap<>();
            Map<String, Integer> counts = new HashMap<>();

            // Group them all
            for (StatementTransaction statementTransaction : unlinked) {
                if (!names.containsKey(statementTransaction.getDescription())) {
                    names.put(statementTransaction.getDescription(), statementTransaction);

                }
                totals.putIfAbsent(statementTransaction.getDescription(), 0.0);
                counts.putIfAbsent(statementTransaction.getDescription(), 0);

                totals.put(statementTransaction.getDescription(), totals.get(statementTransaction.getDescription()) + statementTransaction.getValue());
                counts.put(statementTransaction.getDescription(), counts.get(statementTransaction.getDescription()) + 1);
                total += statementTransaction.getValue();
            }

            // Sort by most important
            java.util.List<Map.Entry<String, Integer>> list = new ArrayList<>(counts.entrySet());
            list.sort((o1, o2) -> totals.get(o2.getKey()).compareTo(totals.get(o1.getKey())));

            // Print all
            list.forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getValue() + " " + NumberFormat.getCurrencyInstance(Locale.JAPAN).format(totals.get(stringIntegerEntry.getKey())) + " " + stringIntegerEntry.getKey()));
            System.out.println();
            System.out.println(total);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyUpdate() {
        SwingUtilities.invokeLater(this::update);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        periodPanel.update();
        fundPanel.update();
        databasePanel.update();
        summaryGraphPanel.update();
        summaryPanel.update();
        receiptPanel.update();
        recurringPaymentPanel.update();
        categorySetPanel.update();
        bankSummaryPanel.update();
        statementPanel.update();
    }
}
