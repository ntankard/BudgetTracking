package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary_Table;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryFundTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import java.awt.*;

public class PeriodSummary_TransferPanel extends UpdatableJPanel {
    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary_Table periodSummary_Table_panel;
    private DataObject_DisplayList<CategoryFundTransfer> periodFundTransfer_panel;

    /**
     * Constructor
     */
    public PeriodSummary_TransferPanel(Period core, Updatable master) {
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

        periodSummary_Table_panel = new PeriodSummary_Table(core, true, this);

        periodFundTransfer_panel = new DataObject_DisplayList<>(CategoryFundTransfer.class, new Children_Set<>(CategoryFundTransfer.class, core), false, this);
        periodFundTransfer_panel.addControlButtons(new CategoryFundTransfer_ElementController(core, this));

        periodFundTransfer_panel.setTitle("Other Transfer");

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 6;
        summaryContainer_C.gridwidth = 2;
        this.add(periodSummary_Table_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        this.add(periodFundTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        periodFundTransfer_panel.update();

        periodSummary_Table_panel.update();
    }
}
