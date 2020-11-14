package com.ntankard.tracking.dispaly.frames.mainFrame.funds.individualFund;

import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.FundEvent_Summary;
import com.ntankard.tracking.dispaly.util.panels.Object_DisplayList;

import javax.swing.*;
import java.awt.*;

import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.INFO_DISPLAY;

public class IndividualFundPanel extends UpdatableJPanel {

    // Core Data
    private final FundEvent core;

    // The GUI components
    private Object_DisplayList<FundEvent_Summary> fundEventSummary_panel;
    private FundTransactionList fundTransactionList;
    private SumGraph sumGraph;

    /**
     * Constructor
     */
    public IndividualFundPanel(FundEvent core, Updatable master) {
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

        JTabbedPane master_tPanel = new JTabbedPane();

        fundEventSummary_panel = new Object_DisplayList<>(core.getTrackingDatabase().getSchema(), FundEvent_Summary.class, new OneParent_Children_Set<>(FundEvent_Summary.class, core), false, this);
        fundEventSummary_panel.setVerbosity(INFO_DISPLAY);
        master_tPanel.addTab("Summary", fundEventSummary_panel);

        fundTransactionList = new FundTransactionList(core, this);
        master_tPanel.addTab("List", fundTransactionList);

        sumGraph = new SumGraph(core, this);
        master_tPanel.addTab("Sum", sumGraph);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        fundEventSummary_panel.update();
        fundTransactionList.update();
        sumGraph.update();
    }
}
