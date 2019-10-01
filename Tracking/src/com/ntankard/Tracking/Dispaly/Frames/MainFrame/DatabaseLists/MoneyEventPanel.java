package com.ntankard.Tracking.Dispaly.Frames.MainFrame.DatabaseLists;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
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
    private List<NonPeriodFundTransfer> nonPeriodFundTransfer_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Transaction> transaction_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DynamicGUI_DisplayList<NonPeriodFundTransfer> nonPeriodFundTransfer_panel;

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
        nonPeriodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(nonPeriodFundTransfer_list, new MemberClass(NonPeriodFundTransfer.class), true, ALWAYS_DISPLAY, this);

        transaction_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        categoryTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        periodTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());
        nonPeriodFundTransfer_panel.getMainPanel().setLocaleInspector(new MoneyEventLocaleInspector());

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        master_tPanel.addTab("Period Transfer", periodTransfer_panel);
        master_tPanel.addTab("Non Period Fund Transfer", nonPeriodFundTransfer_panel);

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
        nonPeriodFundTransfer_list.clear();

        transaction_list.addAll(TrackingDatabase.get().getTransactions());
        categoryTransfer_list.addAll(TrackingDatabase.get().getCategoryTransfers());
        periodTransfer_list.addAll(TrackingDatabase.get().getPeriodTransfers());
        nonPeriodFundTransfer_list.addAll(TrackingDatabase.get().getNonPeriodFundTransfers());

        transaction_panel.update();
        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        nonPeriodFundTransfer_panel.update();
    }
}
