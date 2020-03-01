package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import java.awt.*;

public class FundTransactionList extends UpdatableJPanel {

    // Core Data
    private FundEvent core;

    // The GUI components
    private DataObject_DisplayList<FundTransfer> periodFundPanel;

    /**
     * Constructor
     */
    public FundTransactionList(FundEvent core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;

        periodFundPanel = new DataObject_DisplayList<>(FundTransfer.class, new OneParent_Children_Set<>(FundTransfer.class, core), this);
        this.add(periodFundPanel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodFundPanel.update();
    }
}
