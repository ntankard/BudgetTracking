package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.NoneFundEvent;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.FixedPeriodFundEvent_ElementController;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.NoneFundEvent_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;

public class FundEventsPanel extends UpdatableJPanel {

    private DataObject_DisplayList<FixedPeriodFundEvent> fixedPeriod_panel;
    private DataObject_DisplayList<NoneFundEvent> noneFundEvent_panel;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public FundEventsPanel(Updatable master) {
        super(master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JTabbedPane rootTabbedPane = new JTabbedPane();

        fixedPeriod_panel = new DataObject_DisplayList<>(FixedPeriodFundEvent.class, this);
        fixedPeriod_panel.addControlButtons(new FixedPeriodFundEvent_ElementController(this));
        rootTabbedPane.addTab("Fixed", fixedPeriod_panel);

        noneFundEvent_panel = new DataObject_DisplayList<>(NoneFundEvent.class, this);
        noneFundEvent_panel.addControlButtons(new NoneFundEvent_ElementController(this));
        rootTabbedPane.addTab("None", noneFundEvent_panel);

        this.add(rootTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        fixedPeriod_panel.update();
        noneFundEvent_panel.update();
    }
}
