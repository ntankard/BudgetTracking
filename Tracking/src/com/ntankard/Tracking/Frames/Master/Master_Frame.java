package com.ntankard.Tracking.Frames.Master;

import com.ntankard.DynamicGUI.Util.Swing.Containers.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;

import javax.swing.*;
import java.awt.*;

public class Master_Frame extends JPanel implements Updatable {

    // Core Data
    private TrackingDatabase trackingDatabase;

    // The GUI components
    private PeriodsPanel periodPanel;
    private TransferPanel transferPanel;
    private BaseTypePanel baseTypePanel;

    /**
     * Create and open the tracking frame
     *
     * @param trackingDatabase The data use to populate the frame
     */
    public static void open(TrackingDatabase trackingDatabase) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Budget");
            _frame.setContentPane(new Master_Frame(trackingDatabase));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     *
     * @param trackingDatabase The data use to populate the frame
     */
    private Master_Frame(TrackingDatabase trackingDatabase) {
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

        JButton save_btn = new JButton("Save");
        save_btn.addActionListener(e -> TrackingDatabase_Reader.save(trackingDatabase, "C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData"));

        JButton update_btn = new JButton("Update");
        update_btn.addActionListener(e -> notifyUpdate());

        ButtonPanel btnPanel = new ButtonPanel();
        btnPanel.addButton(save_btn);
        btnPanel.addButton(update_btn);

        this.add(btnPanel, BorderLayout.NORTH);

        periodPanel = new PeriodsPanel(trackingDatabase, this);
        transferPanel = new TransferPanel(trackingDatabase, this);
        baseTypePanel = new BaseTypePanel(trackingDatabase, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Period", periodPanel);
        master_tPanel.addTab("Transfers", transferPanel);
        master_tPanel.addTab("Base Type", baseTypePanel);

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
    }
}
