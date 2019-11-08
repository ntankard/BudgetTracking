package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import javax.swing.*;
import java.awt.*;

public class IndividualFundPanel extends UpdatableJPanel {

    // Core Data
    private Fund core;

    // The GUI components
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
        fundTransactionList.update();
        sumGraph.update();
    }
}
