package com.ntankard.Tracking.Dispaly.Frames.MainFrame.DatabaseLists;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.NonPeriodFund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class MoneyContainerPanel extends UpdatableJPanel {

    // The data displayed (clone of the data in the database)
    private List<NonPeriodFund> nonPeriodFund_list = new ArrayList<>();
    private List<Period> period_list = new ArrayList<>();
    private List<Statement> statement_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<NonPeriodFund> nonPeriodFund_panel;
    private DynamicGUI_DisplayList<Period> period_panel;
    private DynamicGUI_DisplayList<Statement> statement_panel;

    /**
     * Constructor
     */
    public MoneyContainerPanel(Updatable master) {
        super(master);
        createUIComponents();

        // The GUI components
        nonPeriodFund_panel = DynamicGUI_DisplayList.newIntractableTable(nonPeriodFund_list, new MemberClass(NonPeriodFund.class), true, ALWAYS_DISPLAY, this);
        period_panel = DynamicGUI_DisplayList.newIntractableTable(period_list, new MemberClass(Period.class), true, ALWAYS_DISPLAY, this);
        statement_panel = DynamicGUI_DisplayList.newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("NonPeriodFund", nonPeriodFund_panel);
        master_tPanel.addTab("Periods", period_panel);
        master_tPanel.addTab("Statement", statement_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        nonPeriodFund_list.clear();
        period_list.clear();
        statement_list.clear();

        nonPeriodFund_list.addAll(TrackingDatabase.get().getNonPeriodFunds());
        period_list.addAll(TrackingDatabase.get().getPeriods());
        statement_list.addAll(TrackingDatabase.get().getStatements());

        nonPeriodFund_panel.update();
        period_panel.update();
        statement_panel.update();
    }
}
