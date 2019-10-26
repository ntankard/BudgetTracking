package com.ntankard.Tracking.Dispaly.DataObjectPanels;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.Dispaly.Frames.Statement_Frame;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;
import com.ntankard.Tracking.Dispaly.Util.Set.Children_Set;

import java.util.List;

import static com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class StatementPanel extends DataObject_DisplayList<Statement> {

    /**
     * Constructor
     */
    public StatementPanel(Period core, Updatable master) {
        super(Statement.class, new Children_Set<>(Statement.class, core), false, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", this, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = this.getMainPanel().getSelectedItems();
            Statement_Frame.open((Statement) selected.get(0), this);
        });
        this.addButton(manageStatementBtn);
    }
}
