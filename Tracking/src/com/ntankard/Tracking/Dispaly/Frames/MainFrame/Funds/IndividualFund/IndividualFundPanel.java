package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Fund_Summary;
import com.ntankard.Tracking.Dispaly.Util.Comparators.FundSummary_Comparator;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class IndividualFundPanel extends UpdatableJPanel {

    // Core Data
    private Fund core;

    // The GUI components
    private DataObject_DisplayList<Fund_Summary> fundSummary_panel;
    private FundTransactionList fundTransactionList;
    private SumGraph sumGraph;

    /**
     * Constructor
     */
    public IndividualFundPanel(Fund core, Updatable master) {
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

        fundSummary_panel = new DataObject_DisplayList<>(Fund_Summary.class, new Children_Set<>(Fund_Summary.class, core), false, this);
        fundSummary_panel.setVerbosity(INFO_DISPLAY);
        fundSummary_panel.setComparator(new FundSummary_Comparator());
        master_tPanel.addTab("Summary", fundSummary_panel);

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
        fundSummary_panel.update();
        fundTransactionList.update();
        sumGraph.update();
    }
}
