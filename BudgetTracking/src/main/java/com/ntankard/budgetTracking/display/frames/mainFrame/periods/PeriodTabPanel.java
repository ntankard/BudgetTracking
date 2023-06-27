package com.ntankard.budgetTracking.display.frames.mainFrame.periods;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.Period_Summary;
import com.ntankard.budgetTracking.display.frames.mainFrame.periods.individualPeriod.IndividualPeriodPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Single_OneParent_Children_Set;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodTabPanel extends UpdatableJPanel {

    // Core database
    private final Database database;

    // The data displayed (clone of the data in the database)
    private final List<Period> periods = new ArrayList<>();

    // The GUI components
    private final List<IndividualPeriodPanel> periodsPanels = new ArrayList<>();
    private JTabbedPane master_tPanel = new JTabbedPane();

    /**
     * Constructor
     */
    public PeriodTabPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
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
     * @inheritDoc
     */
    @Override
    public void update() {
        rebuildPeriods();

        periodsPanels.forEach(IndividualPeriodPanel::update);

        for (int i = 0; i < master_tPanel.getTabCount(); i++) {
            Component component = master_tPanel.getComponentAt(i);
            if (component instanceof IndividualPeriodPanel) {
                IndividualPeriodPanel individualPeriodPanel = (IndividualPeriodPanel) component;
                if (!new Single_OneParent_Children_Set<>(Period_Summary.class, individualPeriodPanel.getCore()).getItem().isValid()) {
                    master_tPanel.setBackgroundAt(i, Color.RED);
                } else {
                    master_tPanel.setBackgroundAt(i, null);
                }
            } else if (component instanceof JTabbedPane) {
                JTabbedPane jTabbedPane = (JTabbedPane) component;
                boolean anyFault = false;
                for (int j = 0; j < jTabbedPane.getTabCount(); j++) {
                    IndividualPeriodPanel individualPeriodPanel = (IndividualPeriodPanel) jTabbedPane.getComponentAt(j);

                    if (!new Single_OneParent_Children_Set<>(Period_Summary.class, individualPeriodPanel.getCore()).getItem().isValid()) {
                        jTabbedPane.setBackgroundAt(j, Color.RED);
                        anyFault = true;
                    } else {
                        jTabbedPane.setBackgroundAt(j, null);
                    }
                }
                if (anyFault) {
                    master_tPanel.setBackgroundAt(i, Color.RED);
                } else {
                    master_tPanel.setBackgroundAt(i, null);
                }
            } else {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Are there new ore removed period what warrant a complete panel regeneration?
     *
     * @return True if there is new or removed period what warrant a complete panel regeneration
     */
    private boolean hasPeriodsChanged() {
        if (database.get(Period.class).size() != periods.size()) {
            return true;
        }

        for (int i = 0; i < periods.size(); i++) {
            if (!periods.get(i).equals(database.get(Period.class).get(i))) {
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

            periods.addAll(database.get(Period.class));

            Integer year = null;
            JTabbedPane tab = null;
            for (Period period : periods) {

                // Create the core panel
                IndividualPeriodPanel panel = new IndividualPeriodPanel(period, this);

                if (period instanceof ExistingPeriod) { // Group based on year
                    ExistingPeriod existingPeriod = (ExistingPeriod) period;
                    Integer thisYear = existingPeriod.getYear();

                    if (!thisYear.equals(year)) {
                        // New year, create a new top level panel
                        year = thisYear;
                        tab = new JTabbedPane();
                        master_tPanel.addTab(String.valueOf(year), tab);
                    }

                    // Add the child
                    periodsPanels.add(panel);
                    tab.addTab(period.toString(), panel);

                } else { // Single period, top level panel
                    periodsPanels.add(panel);
                    master_tPanel.addTab(period.toString(), panel);
                }
            }

            JTabbedPane jTabbedPane = (JTabbedPane) master_tPanel.getComponentAt(master_tPanel.getTabCount() - 2);
            master_tPanel.setSelectedIndex(master_tPanel.getTabCount() - 2);
            jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount() - 1);
        }
    }
}
