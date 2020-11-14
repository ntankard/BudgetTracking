package com.ntankard.tracking.dispaly.frames.mainFrame;

import com.ntankard.dynamicGUI.gui.util.containers.ButtonPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Reader;
import com.ntankard.tracking.dispaly.frames.mainFrame.funds.FundPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.image.ReceiptPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.periods.PeriodTabPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.summaryGraphs.SummaryGraphPanel;

import javax.swing.*;
import java.awt.*;

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

    private String savePath;

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Create and open the tracking frame
     */
    public static void open(TrackingDatabase trackingDatabase, String savePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new Master_Frame(trackingDatabase, savePath));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     */
    private Master_Frame(TrackingDatabase trackingDatabase, String savePath) {
        this.trackingDatabase = trackingDatabase;
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
            TrackingDatabase_Reader.save(trackingDatabase, savePath);
        });

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        JButton addPeriod_btn = new JButton("Add Period");
        addPeriod_btn.addActionListener(e -> {
            ExistingPeriod last = trackingDatabase.get(ExistingPeriod.class).get(trackingDatabase.get(ExistingPeriod.class).size() - 1);
            Period period = last.generateNext();
            period.add();
            notifyUpdate();
        });

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(save_btn);
        btnPanel.addButton(update_btn);
        btnPanel.addButton(addPeriod_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodTabPanel(trackingDatabase, this);
        fundPanel = new FundPanel(trackingDatabase, this);
        databasePanel = new DatabasePanel(trackingDatabase, this);
        summaryGraphPanel = new SummaryGraphPanel(trackingDatabase, this);
        summaryPanel = new SummaryPanel(trackingDatabase, this);
        receiptPanel = new ReceiptPanel(trackingDatabase, this);
        recurringPaymentPanel = new RecurringPaymentPanel(trackingDatabase, this);
        categorySetPanel = new CategorySetPanel(trackingDatabase, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Periods", periodPanel);
        master_tPanel.addTab("Fund", fundPanel);
        master_tPanel.addTab("Database", databasePanel);
        master_tPanel.addTab("Summary Graphs", summaryGraphPanel);
        master_tPanel.addTab("Summary", summaryPanel);
        master_tPanel.addTab("Receipts", receiptPanel);
        master_tPanel.addTab("Recurring Payments", recurringPaymentPanel);
        master_tPanel.addTab("Category Set", categorySetPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
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
        periodPanel.update();
        fundPanel.update();
        databasePanel.update();
        summaryGraphPanel.update();
        summaryPanel.update();
        receiptPanel.update();
        recurringPaymentPanel.update();
        categorySetPanel.update();
    }
}
