package com.ntankard.statementParser.display.frames.mainFrame;

import com.ntankard.dynamicGUI.gui.util.containers.ButtonPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.frames.mainFrame.DatabasePanel;

import javax.swing.*;
import java.awt.*;

public class MasterFrame extends JPanel implements Updatable {

    // Core database
    private final Database database;

    private DatabasePanel databasePanel;
    private SummaryPanel summaryPanel;
    private PeriodSummaryPanel periodSummaryPanel;
    private SmallPeriodSummaryPanel smallPeriodSummaryPanel;

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

            showOnScreen(1, _frame);

            _frame.setExtendedState(_frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        });
    }

    /**
     * Put the frame on a specific screen
     *
     * @param screen The Screen num to put it on
     * @param frame  The frame to displayt
     */
    public static void showOnScreen(int screen, JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        int width = 0, height = 0;

        width = gd[screen].getDefaultConfiguration().getBounds().width;
        height = gd[screen].getDefaultConfiguration().getBounds().height;
        frame.setLocation(
                ((width / 2) - (frame.getSize().width / 2)) + gd[screen].getDefaultConfiguration().getBounds().x,
                ((height / 2) - (frame.getSize().height / 2)) + gd[screen].getDefaultConfiguration().getBounds().y
        );
        frame.setVisible(true);
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

        databasePanel = new DatabasePanel(database, this);
        summaryPanel = new SummaryPanel(database, this);
        periodSummaryPanel = new PeriodSummaryPanel(database, this);
        smallPeriodSummaryPanel = new SmallPeriodSummaryPanel(database, this);

        JTabbedPane master_tPanel = new JTabbedPane();

        master_tPanel.addTab("PeriodSummary", periodSummaryPanel);
        master_tPanel.addTab("SmallPeriodSummary", smallPeriodSummaryPanel);
        master_tPanel.addTab("Summary", summaryPanel);
        master_tPanel.addTab("Database", databasePanel);

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
        periodSummaryPanel.update();
        summaryPanel.update();
        databasePanel.update();
        smallPeriodSummaryPanel.update();
    }
}
