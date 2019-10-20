package com.ntankard.Tracking.Dispaly.Frames.MainFrame.DatabaseLists;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.MoneyEventLocaleInspector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class MoneyEventPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<Transaction> transaction_list = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();
    private List<PeriodFundTransfer> periodFundTransfer_list = new ArrayList<>();
    private List<FundChargeTransfer> fundChargeTransfer_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DynamicGUI_DisplayList<PeriodFundTransfer> periodFundTransfer_panel;
    private DynamicGUI_DisplayList<FundChargeTransfer> fundChargeTransfer_panel;

    /**
     * Constructor
     */
    public MoneyEventPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // The GUI components
        transaction_panel = DynamicGUI_DisplayList.newIntractableTable(transaction_list, new MemberClass(Transaction.class), true, ALWAYS_DISPLAY, this);
        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), true, ALWAYS_DISPLAY, this);
        periodTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodTransfer_list, new MemberClass(PeriodTransfer.class), true, ALWAYS_DISPLAY, this);
        periodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodFundTransfer_list, new MemberClass(PeriodFundTransfer.class), true, ALWAYS_DISPLAY, this);
        fundChargeTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(fundChargeTransfer_list, new MemberClass(FundChargeTransfer.class), true, ALWAYS_DISPLAY, this);

        transaction_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        categoryTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        periodTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        periodFundTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        fundChargeTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Transaction", transaction_panel);
        master_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        master_tPanel.addTab("Period Transfer", periodTransfer_panel);
        master_tPanel.addTab("Non Period Fund Transfer", periodFundTransfer_panel);
        master_tPanel.addTab("Non Period Fund Charge Transfer", fundChargeTransfer_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        transaction_list.clear();
        categoryTransfer_list.clear();
        periodTransfer_list.clear();
        periodFundTransfer_list.clear();
        fundChargeTransfer_list.clear();

        transaction_list.addAll(TrackingDatabase.get().get(Transaction.class));
        categoryTransfer_list.addAll(TrackingDatabase.get().get(CategoryTransfer.class));
        periodTransfer_list.addAll(TrackingDatabase.get().get(PeriodTransfer.class));
        periodFundTransfer_list.addAll(TrackingDatabase.get().get(PeriodFundTransfer.class));
        fundChargeTransfer_list.addAll(TrackingDatabase.get().get(FundChargeTransfer.class));

        transaction_panel.update();
        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        periodFundTransfer_panel.update();
        fundChargeTransfer_panel.update();
    }
}
