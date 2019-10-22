package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod.IndividualPeriodPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodTabPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<Period> periods = new ArrayList<>();
    private List<IndividualPeriodPanel> periodsPanels = new ArrayList<>();

    // The GUI components
    private JTabbedPane master_tPanel = new JTabbedPane();

    /**
     * Constructor
     */
    public PeriodTabPanel(Updatable master) {
        super(master);
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

        for (IndividualPeriodPanel periodPanel : periodsPanels) {
            periodPanel.update();
        }
    }

    /**
     * Are there new ore removed period what warrant a complete panel regeneration?
     *
     * @return True if there is new or removed period what warrant a complete panel regeneration
     */
    private boolean hasPeriodsChanged() {
        if (TrackingDatabase.get().get(Period.class).size() != periods.size()) {
            return true;
        }

        for (int i = 0; i < periods.size(); i++) {
            if (!periods.get(i).equals(TrackingDatabase.get().get(Period.class).get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Regenerate all the period tabs
     */
    private void rebuildPeriods() {
        if (hasPeriodsChanged()) {

            periods.clear();
            periodsPanels.clear();

            master_tPanel.removeAll();

            periods.addAll(TrackingDatabase.get().get(Period.class));
            for (Period period : periods) {
                IndividualPeriodPanel panel = new IndividualPeriodPanel(period, this);
                periodsPanels.add(panel);
                master_tPanel.addTab(period.getId(), panel);
            }
        }
    }
}
