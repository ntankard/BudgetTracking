package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataObject_TabDisplayList extends UpdatableJPanel {

    // The GUI components
    private List<DataObject_DisplayList> tabs = new ArrayList<>();
    private JTabbedPane master_tPanel;

    /**
     * Constructor
     */
    public DataObject_TabDisplayList(Updatable master) {
        super(master);
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
     * Add a new DataObject_DisplayList
     *
     * @param name   The name to put on the tab
     * @param tClass The class used to build the tab
     * @param <T>    tClass
     */
    public <T extends DataObject> void add(String name, Class<T> tClass) {
        DataObject_DisplayList list = new DataObject_DisplayList<>(tClass, this);

        tabs.add(list);
        master_tPanel.addTab(name, list);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        for (DataObject_DisplayList tab : tabs) {
            tab.update();
        }
    }
}
