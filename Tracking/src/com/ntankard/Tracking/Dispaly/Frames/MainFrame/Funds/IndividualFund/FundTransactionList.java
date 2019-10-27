package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;
import com.ntankard.Tracking.Dispaly.Util.Set.Children_Set;

import java.awt.*;

public class FundTransactionList extends UpdatableJPanel {

    // Core Data
    private Fund core;

    // The GUI components
    private DataObject_DisplayList<PeriodFundTransfer> periodFundPanel;
    private DataObject_DisplayList<FundChargeTransfer> fundChargePanel;

    /**
     * Constructor
     */
    public FundTransactionList(Fund core, Updatable master) {
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

        periodFundPanel = new DataObject_DisplayList<>(PeriodFundTransfer.class, new Children_Set<>(PeriodFundTransfer.class, core), this);
        this.add(periodFundPanel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
        fundChargePanel = new DataObject_DisplayList<>(FundChargeTransfer.class, new Children_Set<>(FundChargeTransfer.class, core), this);
        this.add(fundChargePanel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodFundPanel.update();
        fundChargePanel.update();
    }
}
