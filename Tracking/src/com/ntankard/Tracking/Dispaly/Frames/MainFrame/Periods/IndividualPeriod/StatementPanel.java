package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.Dispaly.Frames.Statement_Frame;
import com.ntankard.Tracking.Dispaly.Util.StatementLocaleInspector;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.newIntractableTable;

public class StatementPanel extends UpdatableJScrollPane {

    // Core Data
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<Statement> statement_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Statement> statement_panel;

    /**
     * Constructor
     */
    public StatementPanel(Period core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        statement_panel = newIntractableTable(statement_list, new MemberClass(Statement.class), false, ALWAYS_DISPLAY, this);
        statement_panel.getMainPanel().setLocaleInspector(new StatementLocaleInspector());

        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", statement_panel, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open((Statement) selected.get(0), this);
        });
        statement_panel.addButton(manageStatementBtn);

        this.setViewportView(statement_panel);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        statement_list.clear();

        statement_list.addAll(core.getChildren(Statement.class));

        statement_panel.update();
    }
}
