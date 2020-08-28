package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataObject_VerbosityDisplayList<T extends DataObject> extends UpdatableJPanel {

    /**
     * The supported verbosity levels
     */
    private static final String[] NAMES = {"Always", "Info", "Debug", "Trace"};

    /**
     * The type to general
     */
    private final Class<T> tClass;

    /**
     * The source of data for the list
     */
    private ObjectSet<T> objectSet = null;

    // The GUI components
    private final List<DataObject_DisplayList<T>> tabs = new ArrayList<>();

    /**
     * Constructor
     */
    public DataObject_VerbosityDisplayList(Class<T> tClass, Updatable master) {
        super(master);
        this.tClass = tClass;
        createUIComponents();
    }

    /**
     * Constructor
     */
    public DataObject_VerbosityDisplayList(Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        super(master);
        this.tClass = tClass;
        this.objectSet = objectSet;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        tabs.clear();

        JTabbedPane master_tPanel = new JTabbedPane();
        JPanel allTab = new JPanel(new GridBagLayout());

        GridBagConstraints allTab_C = new GridBagConstraints();
        allTab_C.fill = GridBagConstraints.BOTH;
        allTab_C.weightx = 1;
        allTab_C.weighty = 1;
        allTab_C.gridy = 0;
        allTab_C.gridx = 0;

        master_tPanel.addTab("All", allTab);

        for (int i = 0; i < NAMES.length; i++) {
            DataObject_DisplayList<T> single;
            if (objectSet == null) {
                single = new DataObject_DisplayList<>(tClass, false, this);
            } else {
                single = new DataObject_DisplayList<>(tClass, objectSet, false, this);
            }
            single.setVerbosity(i);
            master_tPanel.addTab(NAMES[i], single);

            DataObject_DisplayList<T> all;
            if (objectSet == null) {
                all = new DataObject_DisplayList<>(tClass, false, this);
            } else {
                all = new DataObject_DisplayList<>(tClass, objectSet, false, this);
            }

            all.setVerbosity(i);
            all.setBorder(BorderFactory.createTitledBorder(NAMES[i]));
            allTab.add(all, allTab_C);
            allTab_C.gridy++;
        }

        //master_tPanel.addTab("All", allTab);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        for (DataObject_DisplayList<T> tab : tabs) {
            tab.update();
        }
    }
}