package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Containers.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.FundTabPanel;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.PeriodTabPanel;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs.SummaryGraphPanel;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_VerbosityTabDisplayList;

import javax.swing.*;
import java.awt.*;

public class Master_Frame extends JPanel implements Updatable {

    // The GUI components
    private PeriodTabPanel periodPanel;
    private FundTabPanel fundTabPanel;
    private DataObject_VerbosityTabDisplayList transferPanel;
    private DataObject_VerbosityTabDisplayList baseTypePanel;
    private DataObject_VerbosityTabDisplayList moneyContainerPanel;
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
            Period period = last.generateNext();
            TrackingDatabase.get().add(period);
            notifyUpdate();
        });

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(save_btn);
        btnPanel.addButton(update_btn);
        btnPanel.addButton(addPeriod_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodTabPanel(this);
        fundTabPanel = new FundTabPanel(this);

        transferPanel = new DataObject_VerbosityTabDisplayList(this);
        transferPanel.add("Transaction", Transaction.class);
        transferPanel.add("Period Fund Transfer", PeriodFundTransfer.class);
        transferPanel.add("Fund Charge Transfer", FundChargeTransfer.class);
        transferPanel.add("StatementEnd", StatementEnd.class);

        baseTypePanel = new DataObject_VerbosityTabDisplayList(this);
        baseTypePanel.add("Category", Category.class);
        baseTypePanel.add("Currency", Currency.class);
        baseTypePanel.add("Bank", Bank.class);
        baseTypePanel.add("Fund Event", FundEvent.class);

        moneyContainerPanel = new DataObject_VerbosityTabDisplayList(this);
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
        master_tPanel.addTab("Fund", fundTabPanel);
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
        fundTabPanel.update();
        transferPanel.update();
        baseTypePanel.update();
        moneyContainerPanel.update();
        summaryGraphPanel.update();
    }
}
