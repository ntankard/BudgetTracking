package com.ntankard.budgetTracking.display.frames.mainFrame.funds.individualFund;

import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;

import java.awt.*;

public class FundTransactionList extends UpdatableJPanel {

    // Core Data
    private FundEvent core;

    // The GUI components
    private DataObject_DisplayList<Transfer> periodFundPanel;

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

        periodFundPanel = new DataObject_DisplayList<>(core.getTrackingDatabase().getSchema(), Transfer.class, new OneParent_Children_Set<>(Transfer.class, core), this);
        this.add(periodFundPanel, summaryContainer_C);

        summaryContainer_C.gridx = 1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        periodFundPanel.update();
    }
}
