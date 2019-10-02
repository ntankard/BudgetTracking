package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets.PeriodType_Set;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.NonPeriodTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.PeriodTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.MoneyEventLocaleInspector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class PeriodSummary_TransferPanel extends UpdatableJPanel {
    // Core Data
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<NonPeriodFundTransfer> nonPeriodFundTransfer_list = new ArrayList<>();
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<NonPeriodFundTransfer> nonPeriodFundTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;

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

        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), false, true, ALWAYS_DISPLAY, new CategoryTransfer_ElementController(core, this), this, TrackingDatabase.get());
        periodTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodTransfer_list, new MemberClass(PeriodTransfer.class), false, true, ALWAYS_DISPLAY, new PeriodTransfer_ElementController(core, this), this, TrackingDatabase.get());
        nonPeriodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(nonPeriodFundTransfer_list, new MemberClass(NonPeriodFundTransfer.class), false, true, ALWAYS_DISPLAY, new NonPeriodTransfer_ElementController(core, this), this, TrackingDatabase.get());

        categoryTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        periodTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        nonPeriodFundTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());

        categoryTransfer_panel.setNorth(new JLabel("Category Transfer"));
        periodTransfer_panel.setNorth(new JLabel("Periods Transfer"));
        nonPeriodFundTransfer_panel.setNorth(new JLabel("Other Transfer"));

        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 6;
        summaryContainer_C.gridwidth = 3;
        this.add(periodSummary_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        this.add(categoryTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
        this.add(periodTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 2;
        this.add(nonPeriodFundTransfer_panel, summaryContainer_C);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        categoryTransfer_list.clear();
        periodTransfer_list.clear();
        nonPeriodFundTransfer_list.clear();

        categoryTransfer_list.addAll(new PeriodType_Set<CategoryTransfer>(core, CategoryTransfer.class).getMoneyEvents());
        periodTransfer_list.addAll(new PeriodType_Set<PeriodTransfer>(core, PeriodTransfer.class).getMoneyEvents());
        nonPeriodFundTransfer_list.addAll(new PeriodType_Set<NonPeriodFundTransfer>(core, NonPeriodFundTransfer.class).getMoneyEvents());

        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        nonPeriodFundTransfer_panel.update();

        periodSummary_panel.update();
    }
}
