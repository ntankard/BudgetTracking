package com.ntankard.tracking.dispaly.util.panels;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataObject_VerbosityTabDisplayList extends UpdatableJPanel {

    // The GUI components
    private List<DataObject_VerbosityDisplayList> tabs = new ArrayList<>();
    private JTabbedPane master_tPanel;

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Constructor
     */
    public DataObject_VerbosityTabDisplayList(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.master_tPanel = new JTabbedPane();
        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * Add a new DataObject_VerbosityDisplayList
     *
     * @param name   The name to put on the tab
     * @param tClass The class used to build the tab
     * @param <T>    tClass
     */
    public <T extends DataObject> void add(String name, Class<T> tClass) {
        DataObject_VerbosityDisplayList list = new DataObject_VerbosityDisplayList<>(trackingDatabase, tClass, this);

        tabs.add(list);
        master_tPanel.addTab(name, list);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        for (DataObject_VerbosityDisplayList tab : tabs) {
            tab.update();
        }
    }
}