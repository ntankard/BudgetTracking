package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataObject_VerbosityDisplayList<T extends DataObject> extends UpdatableJPanel {

    /**
     * The supported verbosity levels
     */
    private static String[] NAMES = {"Always", "Info", "Debug", "Trace"};

    /**
     * The type to general
     */
    private Class<T> tClass;

    // The GUI components
    private List<DataObject_DisplayList> tabs = new ArrayList<>();

    /**
     * Constructor
     */
    public DataObject_VerbosityDisplayList(Class<T> tClass, Updatable master) {
        super(master);
        this.tClass = tClass;
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
            DataObject_DisplayList single = new DataObject_DisplayList<>(tClass, false, this);
            single.setVerbosity(i);
            master_tPanel.addTab(NAMES[i], single);

            DataObject_DisplayList all = new DataObject_DisplayList<>(tClass, false, this);
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
        for (DataObject_DisplayList tab : tabs) {
            tab.update();
        }
    }
}
