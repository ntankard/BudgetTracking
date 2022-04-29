package com.ntankard.budgetTracking.display.frames.mainFrame.periods.individualPeriod;

import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;

import javax.swing.*;
import java.awt.*;

public class IndividualPeriodPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary_StatementPanel periodSummary_statementPanel;
    private SummaryPanel summaryPanel;

    /**
     * Constructor
     */
    public IndividualPeriodPanel(Period core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        summaryPanel = new SummaryPanel(core, this);
        this.add(summaryPanel, BorderLayout.NORTH);

        JTabbedPane master_tPanel = new JTabbedPane();

        periodSummary_statementPanel = new PeriodSummary_StatementPanel(core, this);
        master_tPanel.addTab("Statement", periodSummary_statementPanel);

        this.add(periodSummary_statementPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        periodSummary_statementPanel.update();
        summaryPanel.update();
    }
}
