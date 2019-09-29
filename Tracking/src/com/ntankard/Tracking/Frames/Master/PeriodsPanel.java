package com.ntankard.Tracking.Frames.Master;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodsPanel extends UpdatableJPanel {
    // Core Data
    private TrackingDatabase trackingDatabase;

    // The data displayed (clone of the data in the database)
    private List<Period> periods = new ArrayList<>();
    private List<PeriodPanel> periodsPanels = new ArrayList<>();

    // The GUI components
    private JTabbedPane master_tPanel = new JTabbedPane();

    /**
     * Constructor
     */
    public PeriodsPanel(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        master_tPanel = new JTabbedPane();

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        rebuildPeriods();

        for (PeriodPanel periodPanel : periodsPanels) {
            periodPanel.update();
        }
    }

    /**
     * Regenerate all the period tabs
     */
    private void rebuildPeriods() {
        int selected = master_tPanel.getSelectedIndex();
        int[] inSelected = new int[periodsPanels.size()];
        int pastSize = periodsPanels.size();
        for (int i = 0; i < periodsPanels.size(); i++) {
            inSelected[i] = periodsPanels.get(i).master_tPanel.getSelectedIndex();
        }

        periods.clear();
        periodsPanels.clear();

        master_tPanel.removeAll();

        periods.addAll(trackingDatabase.getPeriods());
        for (Period period : periods) {
            PeriodPanel panel = new PeriodPanel(trackingDatabase, period, this);
            periodsPanels.add(panel);
            master_tPanel.addTab(period.getId(), panel);
        }

        if (selected >= 0) {
            master_tPanel.setSelectedIndex(selected);
        }

        for (int i = 0; i < pastSize; i++) {
            periodsPanels.get(i).master_tPanel.setSelectedIndex(inSelected[i]);
        }
    }
}
