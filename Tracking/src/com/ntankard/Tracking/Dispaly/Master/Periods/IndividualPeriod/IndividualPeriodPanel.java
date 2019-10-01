package com.ntankard.Tracking.Dispaly.Master.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;

import javax.swing.*;
import java.awt.*;

public class IndividualPeriodPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary_TransferPanel periodSummary_transferPanel;
    private StatementPanel statementPanel;

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

        // The GUI components
        JTabbedPane master_tPanel = new JTabbedPane();

        periodSummary_transferPanel = new PeriodSummary_TransferPanel(core, this);
        master_tPanel.addTab("Summary", periodSummary_transferPanel);

        statementPanel = new StatementPanel(core, this);
        master_tPanel.addTab("Statements", statementPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodSummary_transferPanel.update();
        statementPanel.update();
    }
}
