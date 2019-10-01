package com.ntankard.Tracking.Dispaly.Master;

import com.ntankard.DynamicGUI.Util.Swing.Containers.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Master.DatabaseLists.MoneyContainerPanel;
import com.ntankard.Tracking.Dispaly.Master.DatabaseLists.ReferenceTypesPanel;
import com.ntankard.Tracking.Dispaly.Master.Periods.PeriodTabPanel;
import com.ntankard.Tracking.Dispaly.Master.DatabaseLists.MoneyEventPanel;

import javax.swing.*;
import java.awt.*;

public class Master_Frame extends JPanel implements Updatable {

    // The GUI components
    private PeriodTabPanel periodPanel;
    private MoneyEventPanel transferPanel;
    private ReferenceTypesPanel baseTypePanel;
    private MoneyContainerPanel moneyContainerPanel;

    /**
     * Create and open the tracking frame
     */
    public static void open() {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new Master_Frame());
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     */
    private Master_Frame() {
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
        save_btn.addActionListener(e -> TrackingDatabase_Reader.save(TrackingDatabase.get(), "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData"));

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(save_btn);
        btnPanel.addButton(update_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodTabPanel(this);
        transferPanel = new MoneyEventPanel(this);
        baseTypePanel = new ReferenceTypesPanel(this);
        moneyContainerPanel = new MoneyContainerPanel(this);

        JTabbedPane databasePanel = new JTabbedPane();
        databasePanel.addTab("Transfers", transferPanel);
        databasePanel.addTab("Base Type", baseTypePanel);
        databasePanel.addTab("Containers", moneyContainerPanel);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Periods", periodPanel);
        master_tPanel.addTab("Database", databasePanel);

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
    }
}