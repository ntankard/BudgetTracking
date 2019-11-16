package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.ExtendedStatementPanel;

import javax.swing.*;
import java.awt.*;

public class IndividualPeriodPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary_StatementPanel periodSummary_statementPanel;
    private PeriodSummary_TransferPanel periodSummary_transferPanel;
    private ExtendedStatementPanel extendedStatementPanel;
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

        periodSummary_transferPanel = new PeriodSummary_TransferPanel(core, this);
        master_tPanel.addTab("Transaction", periodSummary_transferPanel);

        extendedStatementPanel = new ExtendedStatementPanel(core, this);
        master_tPanel.addTab("Statements", extendedStatementPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodSummary_statementPanel.update();
        periodSummary_transferPanel.update();
        extendedStatementPanel.update();
        summaryPanel.update();
    }
}
