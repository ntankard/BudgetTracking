package com.ntankard.tracking.dispaly.frames.mainFrame.funds;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.NoneFundEvent;
import com.ntankard.tracking.dispaly.util.elementControllers.FixedPeriodFundEvent_ElementController;
import com.ntankard.tracking.dispaly.util.elementControllers.NoneFundEvent_ElementController;
import com.ntankard.tracking.dispaly.util.panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;

public class FundEventsPanel extends UpdatableJPanel {

    private DataObject_DisplayList<FixedPeriodFundEvent> fixedPeriod_panel;
    private DataObject_DisplayList<NoneFundEvent> noneFundEvent_panel;

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public FundEventsPanel(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
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

        fixedPeriod_panel = new DataObject_DisplayList<>(trackingDatabase, FixedPeriodFundEvent.class, this);
        fixedPeriod_panel.addControlButtons(new FixedPeriodFundEvent_ElementController(trackingDatabase, this));
        rootTabbedPane.addTab("Fixed", fixedPeriod_panel);

        noneFundEvent_panel = new DataObject_DisplayList<>(trackingDatabase, NoneFundEvent.class, this);
        noneFundEvent_panel.addControlButtons(new NoneFundEvent_ElementController(trackingDatabase, this));
        rootTabbedPane.addTab("None", noneFundEvent_panel);

        this.add(rootTabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        fixedPeriod_panel.update();
        noneFundEvent_panel.update();
    }
}
