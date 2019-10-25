package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets.PeriodType_Set;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Swing.PeriodSummary.PeriodSummary;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.CategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.FundChargeTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.PeriodFundTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.PeriodTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.LocaleInspectors.CurrencyBound_LocaleSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodSummary_TransferPanel extends UpdatableJPanel {
    // Core Data
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<PeriodFundTransfer> periodFundTransfer_list = new ArrayList<>();
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();
    private List<FundChargeTransfer> fundChargeTransfer_list = new ArrayList<>();

    // The GUI components
    private PeriodSummary periodSummary_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<PeriodFundTransfer> periodFundTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DynamicGUI_DisplayList<FundChargeTransfer> fundChargeTransfer_panel;

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

        categoryTransfer_panel = new DynamicGUI_DisplayList<>(categoryTransfer_list, new MemberClass(CategoryTransfer.class), this)
                .setSources(TrackingDatabase.get())
                .setLocaleSource(new CurrencyBound_LocaleSource())
                .addControlButtons(new CategoryTransfer_ElementController(core, this));
        periodTransfer_panel = new DynamicGUI_DisplayList<>(periodTransfer_list, new MemberClass(PeriodTransfer.class), this)
                .setSources(TrackingDatabase.get())
                .setLocaleSource(new CurrencyBound_LocaleSource())
                .addControlButtons(new PeriodTransfer_ElementController(core, this));
        periodFundTransfer_panel = new DynamicGUI_DisplayList<>(periodFundTransfer_list, new MemberClass(PeriodFundTransfer.class), this)
                .setSources(TrackingDatabase.get()).setLocaleSource(new CurrencyBound_LocaleSource())
                .setLocaleSource(new CurrencyBound_LocaleSource())
                .addControlButtons(new PeriodFundTransfer_ElementController(core, this));
        fundChargeTransfer_panel = new DynamicGUI_DisplayList<>(fundChargeTransfer_list, new MemberClass(FundChargeTransfer.class), this)
                .setSources(TrackingDatabase.get())
                .setLocaleSource(new CurrencyBound_LocaleSource())
                .addControlButtons(new FundChargeTransfer_ElementController(core, this));

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
        categoryTransfer_list.clear();
        periodTransfer_list.clear();
        periodFundTransfer_list.clear();
        fundChargeTransfer_list.clear();

        categoryTransfer_list.addAll(new PeriodType_Set<CategoryTransfer>(core, CategoryTransfer.class).getMoneyEvents());
        periodTransfer_list.addAll(new PeriodType_Set<PeriodTransfer>(core, PeriodTransfer.class).getMoneyEvents());
        periodFundTransfer_list.addAll(new PeriodType_Set<PeriodFundTransfer>(core, PeriodFundTransfer.class).getMoneyEvents());
        fundChargeTransfer_list.addAll(new PeriodType_Set<FundChargeTransfer>(core, FundChargeTransfer.class).getMoneyEvents());

        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        periodFundTransfer_panel.update();
        fundChargeTransfer_panel.update();

        periodSummary_panel.update();
    }
}
