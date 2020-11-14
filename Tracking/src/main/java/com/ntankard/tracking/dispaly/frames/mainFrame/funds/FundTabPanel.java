package com.ntankard.tracking.dispaly.frames.mainFrame.funds;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.frames.mainFrame.funds.individualFund.IndividualFundPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FundTabPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<FundEvent> fundEvents = new ArrayList<>();

    // The GUI components
    private JTabbedPane master_tPanel = new JTabbedPane();
    private List<IndividualFundPanel> fundPanels = new ArrayList<>();

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public FundTabPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        master_tPanel = new JTabbedPane();

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        rebuildFunds();

        for (IndividualFundPanel fundPanel : fundPanels) {
            fundPanel.update();
        }
    }

    /**
     * Are there new ore removed period what warrant a complete panel regeneration?
     *
     * @return True if there is new or removed period what warrant a complete panel regeneration
     */
    private boolean hasFundsChanged() {
        if (database.get(FundEvent.class).size() != fundEvents.size()) {
            return true;
        }

        for (int i = 0; i < fundEvents.size(); i++) {
            if (!fundEvents.get(i).equals(database.get(FundEvent.class).get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Regenerate all the period tabs
     */
    private void rebuildFunds() {
        if (hasFundsChanged()) {
            fundEvents.clear();
            fundPanels.clear();

            master_tPanel.removeAll();

            fundEvents.addAll(database.get(FundEvent.class));
            for (FundEvent fundEvent : fundEvents) {
                IndividualFundPanel panel = new IndividualFundPanel(fundEvent, this);
                fundPanels.add(panel);
                master_tPanel.addTab(fundEvent.toString(), panel);
            }
        }
    }
}
