package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Containers.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.PeriodTabPanel;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs.SummaryGraphPanel;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_TabDisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class Master_Frame extends JPanel implements Updatable {

    // The GUI components
    private PeriodTabPanel periodPanel;
    private DataObject_TabDisplayList transferPanel;
    private DataObject_TabDisplayList baseTypePanel;
    private DataObject_TabDisplayList moneyContainerPanel;
    private SummaryGraphPanel summaryGraphPanel;

    private String savePath;

    /**
     * Create and open the tracking frame
     */
    public static void open(String savePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new Master_Frame(savePath));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     */
    private Master_Frame(String savePath) {
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
        save_btn.addActionListener(e -> TrackingDatabase_Reader.save(TrackingDatabase.get(), savePath));

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        JButton addPeriod_btn = new JButton("Add Period");
        addPeriod_btn.addActionListener(e -> {
            Period last = TrackingDatabase.get().get(Period.class).get(TrackingDatabase.get().get(Period.class).size() - 1);
            Period period = Period.Month(last.getNextPeriodTime().get(Calendar.MONTH) + 1, last.getNextPeriodTime().get(Calendar.YEAR));
            TrackingDatabase.get().add(period);
            notifyUpdate();
        });

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(save_btn);
        btnPanel.addButton(update_btn);
        btnPanel.addButton(addPeriod_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodTabPanel(this);

        transferPanel = new DataObject_TabDisplayList(this);
        transferPanel.add("Transaction", Transaction.class);
        transferPanel.add("Category Transfer", CategoryTransfer.class);
        transferPanel.add("Period Transfer", PeriodTransfer.class);
        transferPanel.add("Non Period Fund Transfer", PeriodFundTransfer.class);
        transferPanel.add("Non Period Fund Charge Transfer", FundChargeTransfer.class);

        baseTypePanel = new DataObject_TabDisplayList(this);
        baseTypePanel.add("Category", Category.class);
        baseTypePanel.add("Currency", Currency.class);
        baseTypePanel.add("Bank", Bank.class);
        baseTypePanel.add("Non Period Fund Event", FundEvent.class);

        moneyContainerPanel = new DataObject_TabDisplayList(this);
        moneyContainerPanel.add("Fund", Fund.class);
        moneyContainerPanel.add("Periods", Period.class);
        moneyContainerPanel.add("Statement", Statement.class);

        JTabbedPane databasePanel = new JTabbedPane();
        databasePanel.addTab("Transfers", transferPanel);
        databasePanel.addTab("Base Type", baseTypePanel);
        databasePanel.addTab("Containers", moneyContainerPanel);

        summaryGraphPanel = new SummaryGraphPanel(this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Periods", periodPanel);
        master_tPanel.addTab("Database", databasePanel);
        master_tPanel.addTab("Summary", summaryGraphPanel);

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
        transferPanel.update();
        baseTypePanel.update();
        moneyContainerPanel.update();
        summaryGraphPanel.update();
    }
}
