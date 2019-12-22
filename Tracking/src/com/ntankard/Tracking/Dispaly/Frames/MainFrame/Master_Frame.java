package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Containers.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.FundTabPanel;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.PeriodTabPanel;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs.SummaryGraphPanel;

import javax.swing.*;
import java.awt.*;

public class Master_Frame extends JPanel implements Updatable {

    // The GUI components
    private PeriodTabPanel periodPanel;
    private FundTabPanel fundTabPanel;
    private DatabasePanel databasePanel;
    private SummaryGraphPanel summaryGraphPanel;
    private SummaryPanel summaryPanel;
    private FundEventsPanel fundEventsPanel;

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
        save_btn.addActionListener(e -> {
            TrackingDatabase_Reader.save(TrackingDatabase.get(), savePath);
        });

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        JButton addPeriod_btn = new JButton("Add Period");
        addPeriod_btn.addActionListener(e -> {
            ExistingPeriod last = TrackingDatabase.get().get(ExistingPeriod.class).get(TrackingDatabase.get().get(ExistingPeriod.class).size() - 1);
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
        databasePanel = new DatabasePanel(this);
        summaryGraphPanel = new SummaryGraphPanel(this);
        summaryPanel = new SummaryPanel(this);
        fundEventsPanel = new FundEventsPanel(this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Periods", periodPanel);
        master_tPanel.addTab("Fund", fundTabPanel);
        master_tPanel.addTab("Database", databasePanel);
        master_tPanel.addTab("Summary Graphs", summaryGraphPanel);
        master_tPanel.addTab("Summary", summaryPanel);
        master_tPanel.addTab("Fund Event", fundEventsPanel);

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
        databasePanel.update();
        summaryGraphPanel.update();
        summaryPanel.update();
        fundEventsPanel.update();
    }
}
