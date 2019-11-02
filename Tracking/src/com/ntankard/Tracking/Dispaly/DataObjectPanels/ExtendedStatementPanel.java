package com.ntankard.Tracking.Dispaly.DataObjectPanels;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedStatement;
import com.ntankard.Tracking.DataBase.Interface.Set.ExtendedSets.ExtendedStatement_Set;
import com.ntankard.Tracking.Dispaly.Frames.Statement_Frame;
import com.ntankard.Tracking.Dispaly.Util.Panels.Object_DisplayList;

import java.util.List;

import static com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class ExtendedStatementPanel extends Object_DisplayList<ExtendedStatement> {

    /**
     * Constructor
     */
    public ExtendedStatementPanel(Period core, Updatable master) {
        super(ExtendedStatement.class, new ExtendedStatement_Set(core), false, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", this, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = this.getMainPanel().getSelectedItems();
            Statement_Frame.open(((ExtendedStatement) selected.get(0)).getStatement(), this);
        });
        this.addButton(manageStatementBtn);
    }
}
