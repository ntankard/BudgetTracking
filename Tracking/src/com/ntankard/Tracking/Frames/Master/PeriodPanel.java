package com.ntankard.Tracking.Frames.Master;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Statement;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Statement_Frame;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.PeriodSummary;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.*;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class PeriodPanel extends UpdatableJPanel {
    // Core Data
    private TrackingDatabase trackingDatabase;
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<Statement> statement_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private PeriodSummary periodSummary_panel;

    /**
     * Constructor
     */
    public PeriodPanel(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        statement_panel = newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);
        statement_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Statement statement = (Statement) rowObject;
            if (statement.getIdBank().getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", statement_panel, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open(trackingDatabase, (Statement) selected.get(0), this);
        });
        statement_panel.addButton(manageStatementBtn);

        periodSummary_panel = new PeriodSummary(trackingDatabase, core, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Summary", periodSummary_panel);
        master_tPanel.addTab("Statements", statement_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        statement_list.clear();

        statement_list.addAll(core.getStatements());

        statement_panel.update();
        periodSummary_panel.update();
        periodSummary_panel.update();
    }
}
