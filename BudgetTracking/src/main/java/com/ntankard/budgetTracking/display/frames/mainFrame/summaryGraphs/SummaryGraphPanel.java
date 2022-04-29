package com.ntankard.budgetTracking.display.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;

import javax.swing.*;
import java.awt.*;

public class SummaryGraphPanel extends UpdatableJPanel {

    // The GUI components
    private SavingsGraph savingsGraph;
    private CategoryGraph categoryGraph;
    private SetCategoryGraph setCategoryGraph;
    private FundGraph fundGraph;
    private SumFundGraph sumFundGraph;
    private HolidayGraph holidayGraph;

    private JTabbedPane master_tPanel;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public SummaryGraphPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridLayout(1, 1));

        savingsGraph = new SavingsGraph(database, this);
        categoryGraph = new CategoryGraph(database, this);
        setCategoryGraph = new SetCategoryGraph(database, this);
        fundGraph = new FundGraph(database, this);
        sumFundGraph = new SumFundGraph(database, this);
        holidayGraph = new HolidayGraph(database, this);

        master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Savings", savingsGraph);
        master_tPanel.addTab("Category", categoryGraph);
        master_tPanel.addTab("SetCategory", setCategoryGraph);
        master_tPanel.addTab("Fund", fundGraph);
        master_tPanel.addTab("Fund Sum", sumFundGraph);
        master_tPanel.addTab("Holiday", holidayGraph);

        this.add(master_tPanel);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        savingsGraph.update();
        categoryGraph.update();
        setCategoryGraph.update();
        fundGraph.update();
        sumFundGraph.update();
        holidayGraph.update();
    }
}
