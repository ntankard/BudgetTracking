package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund.IndividualFundPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FundTabPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<Fund> funds = new ArrayList<>();

    // The GUI components
    private JTabbedPane master_tPanel = new JTabbedPane();
    private List<IndividualFundPanel> fundPanels = new ArrayList<>();

    /**
     * Constructor
     */
    public FundTabPanel(Updatable master) {
        super(master);
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
        if (TrackingDatabase.get().get(Fund.class).size() != funds.size()) {
            return true;
        }

        for (int i = 0; i < funds.size(); i++) {
            if (!funds.get(i).equals(TrackingDatabase.get().get(Fund.class).get(i))) {
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
            funds.clear();
            fundPanels.clear();

            master_tPanel.removeAll();

            funds.addAll(TrackingDatabase.get().get(Fund.class));
            for (Fund fund : funds) {
                IndividualFundPanel panel = new IndividualFundPanel(fund, this);
                fundPanels.add(panel);
                master_tPanel.addTab(fund.toString(), panel);
            }
        }
    }
}
