package com.ntankard.databaseViewer.display.frames.mainFrame;

import com.ntankard.databaseViewer.dataBase.type.PrimitiveType;
import com.ntankard.databaseViewer.dataBase.type.dataObjectType.FieldDataObjectType;
import com.ntankard.databaseViewer.dataBase.type.dataObjectType.FileDataObjectType;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;
import com.ntankard.tracking.dispaly.util.panels.DataObject_DisplayList;

import javax.swing.*;
import java.awt.*;

public class TypePanel extends UpdatableJPanel {

    // Core database
    private final Database database;

    private DataObject_DisplayList<FileDataObjectType> file_dataObject_Type_panel;
    private DataObject_DisplayList<FieldDataObjectType> field_dataObject_Type_panel;
    private DataObject_DisplayList<PrimitiveType> primitive_Type_panel;

    /**
     * Constructor
     */
    public TypePanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        file_dataObject_Type_panel = new DataObject_DisplayList<>(database.getSchema(), FileDataObjectType.class, new Full_Set<>(database, FileDataObjectType.class), false, this);
        field_dataObject_Type_panel = new DataObject_DisplayList<>(database.getSchema(), FieldDataObjectType.class, new Full_Set<>(database, FieldDataObjectType.class), false, this);
        primitive_Type_panel = new DataObject_DisplayList<>(database.getSchema(), PrimitiveType.class, new Full_Set<>(database, PrimitiveType.class), false, this);

        JTabbedPane master_tPanel = new JTabbedPane();

        master_tPanel.addTab("File", file_dataObject_Type_panel);
        master_tPanel.addTab("Field", field_dataObject_Type_panel);
        master_tPanel.addTab("Primitive", primitive_Type_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        file_dataObject_Type_panel.update();
        field_dataObject_Type_panel.update();
        primitive_Type_panel.update();
    }
}
