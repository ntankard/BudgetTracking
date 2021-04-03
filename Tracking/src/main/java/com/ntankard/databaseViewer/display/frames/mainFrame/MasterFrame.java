package com.ntankard.databaseViewer.display.frames.mainFrame;

import com.ntankard.dynamicGUI.gui.util.containers.ButtonPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.io.Database_IO;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dispaly.frames.mainFrame.*;
import com.ntankard.tracking.dispaly.frames.mainFrame.funds.FundPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.image.ReceiptPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.periods.PeriodTabPanel;
import com.ntankard.tracking.dispaly.frames.mainFrame.summaryGraphs.SummaryGraphPanel;

import javax.swing.*;
import java.awt.*;

public class MasterFrame extends JPanel implements Updatable {

    // The GUI components

    // Core database
    private final Database database;

    private DeltaPanel deltaPanel;
    private DatabasePanel databasePanel;
    private StructurePanel structurePanel;
    private FullDataPanel fullDataPanel;

    /**
     * Create and open the tracking frame
     */
    public static void open(Database database) {
        SwingUtilities.invokeLater(() -> {
            JFrame _frame = new JFrame("Database Viewer");
            _frame.setContentPane(new MasterFrame(database));
            _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            _frame.pack();
            _frame.setVisible(true);

            _frame.repaint();
        });
    }

    /**
     * Constructor
     */
    private MasterFrame(Database database) {
        this.database = database;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1500, 1000));

        ButtonPanel btnPanel = new ButtonPanel();

        this.add(btnPanel, BorderLayout.NORTH);

        deltaPanel = new DeltaPanel(database, this);
        databasePanel = new DatabasePanel(database, this);
        structurePanel = new StructurePanel(database, this);
        fullDataPanel = new FullDataPanel(database, this);

        JTabbedPane master_tPanel = new JTabbedPane();

        master_tPanel.addTab("DeltaPanel", deltaPanel);
        master_tPanel.addTab("FullDataPanel", fullDataPanel);
        master_tPanel.addTab("Database", databasePanel);
        master_tPanel.addTab("Structure", structurePanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyUpdate() {
        SwingUtilities.invokeLater(this::update);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        deltaPanel.update();
        databasePanel.update();
        structurePanel.update();
        fullDataPanel.update();
    }
}
