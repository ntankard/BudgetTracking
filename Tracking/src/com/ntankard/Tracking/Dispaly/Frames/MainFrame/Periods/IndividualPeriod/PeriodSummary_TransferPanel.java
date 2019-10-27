package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.Dispaly.Util.Set.MoneyEvent_Sets.ContainerType_Set;
import com.ntankard.Tracking.Dispaly.DataObjectPanels.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.FundChargeTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.PeriodFundTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.PeriodTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import java.awt.*;

public class PeriodSummary_TransferPanel extends UpdatableJPanel {
    // Core Data
    private Period core;

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private DataObject_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DataObject_DisplayList<PeriodFundTransfer> periodFundTransfer_panel;
    private DataObject_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DataObject_DisplayList<FundChargeTransfer> fundChargeTransfer_panel;

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

        periodSummary_panel = new PeriodSummary(core, true, this);

        categoryTransfer_panel = new DataObject_DisplayList<>(CategoryTransfer.class, new ContainerType_Set<>(core, CategoryTransfer.class), false, this);
        categoryTransfer_panel.addControlButtons(new CategoryTransfer_ElementController(core, this));

        periodTransfer_panel = new DataObject_DisplayList<>(PeriodTransfer.class, new ContainerType_Set<>(core, PeriodTransfer.class), false, this);
        periodTransfer_panel.addControlButtons(new PeriodTransfer_ElementController(core, this));

        periodFundTransfer_panel = new DataObject_DisplayList<>(PeriodFundTransfer.class, new ContainerType_Set<>(core, PeriodFundTransfer.class), false, this);
        periodFundTransfer_panel.addControlButtons(new PeriodFundTransfer_ElementController(core, this));

        fundChargeTransfer_panel = new DataObject_DisplayList<>(FundChargeTransfer.class, new ContainerType_Set<>(core, FundChargeTransfer.class), false, this);
        fundChargeTransfer_panel.addControlButtons(new FundChargeTransfer_ElementController(core, this));

        categoryTransfer_panel.setTitle("Category Transfer");
        periodTransfer_panel.setTitle("Periods Transfer");
        periodFundTransfer_panel.setTitle("Other Transfer");

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 6;
        summaryContainer_C.gridwidth = 4;
        this.add(periodSummary_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        this.add(categoryTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
        this.add(periodTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 2;
        this.add(periodFundTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 3;
        this.add(fundChargeTransfer_panel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        periodFundTransfer_panel.update();
        fundChargeTransfer_panel.update();

        periodSummary_panel.update();
    }
}
