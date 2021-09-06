package com.ntankard.budgetTracking.display.util.panels;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

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

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public DataObject_VerbosityDisplayList(Database database, Class<T> tClass, Updatable master) {
        super(master);
        this.database = database;
        this.tClass = tClass;
        createUIComponents();
    }

    /**
     * Constructor
     */
    public DataObject_VerbosityDisplayList(Database database, Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        super(master);
        this.database = database;
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
                single = new DataObject_DisplayList<>(database, tClass, false, this);
            } else {
                single = new DataObject_DisplayList<>(database.getSchema(), tClass, objectSet, false, this);
            }
            single.setVerbosity(i);
            master_tPanel.addTab(NAMES[i], single);

            DataObject_DisplayList<T> all;
            if (objectSet == null) {
                all = new DataObject_DisplayList<>(database, tClass, false, this);
            } else {
                all = new DataObject_DisplayList<>(database.getSchema(), tClass, objectSet, false, this);
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
     * @inheritDoc
     */
    @Override
    public void update() {
        for (DataObject_DisplayList<T> tab : tabs) {
            tab.update();
        }
    }
}
