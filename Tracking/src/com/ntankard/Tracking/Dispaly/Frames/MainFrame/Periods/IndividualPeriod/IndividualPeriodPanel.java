package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;

import javax.swing.*;
import java.awt.*;

public class IndividualPeriodPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary_StatementPanel periodSummary_statementPanel;
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

        periodSummary_statementPanel = new PeriodSummary_StatementPanel(core, this);
        master_tPanel.addTab("Statement", periodSummary_statementPanel);

        periodSummary_transferPanel = new PeriodSummary_TransferPanel(core, this);
        master_tPanel.addTab("Transaction", periodSummary_transferPanel);

        statementPanel = new StatementPanel(core, this);
        master_tPanel.addTab("Statements", statementPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodSummary_statementPanel.update();
        periodSummary_transferPanel.update();
        statementPanel.update();
    }
}