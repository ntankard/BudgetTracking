package com.ntankard.Tracking.Dispaly.Util.Panels;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DataObject_DisplayList<T extends DataObject> extends UpdatableJPanel {

    /**
     * The data type to display
     */
    private Class<T> tClass;

    // Display settings
    private int verbosityLevel = ALWAYS_DISPLAY;
    private boolean addFilter = true;

    // The GUI components
    private List<T> coreData = new ArrayList<>();
    private DynamicGUI_DisplayList<T> mainPanel;

    /**
     * Constructor
     */
    public DataObject_DisplayList(Class<T> tClass, Updatable master) {
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
        this.mainPanel = DynamicGUI_DisplayList.newIntractableTable(coreData, new MemberClass(tClass), addFilter, verbosityLevel, this);
        this.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        coreData.clear();
        coreData.addAll(TrackingDatabase.get().get(tClass));
        mainPanel.update();
    }
}
