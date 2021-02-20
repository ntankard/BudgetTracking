package com.ntankard.tracking.dispaly.frames.mainFrame;

import com.ntankard.tracking.dataBase.interfaces.summary.Period_Summary;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.PoolSummary;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.util.panels.DataObject_VerbosityDisplayList;
import com.ntankard.javaObjectDatabase.database.subContainers.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DatabasePanel extends UpdatableJPanel {

    /**
     * The root GUI objects
     */
    private JTabbedPane rootTabbedPane;

    /**
     * All the individual lists for updating
     */
    private List<DataObject_VerbosityDisplayList> updatableList;

    /**
     * The number of objects used to generate the GUI (Used to determine if the structure needs to be updated)
     */
    private int objectTypeSize = 0;

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public DatabasePanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        this.updatableList = new ArrayList<>();
        this.rootTabbedPane = new JTabbedPane();
        this.add(rootTabbedPane);
    }

    /**
     * Create one layer
     *
     * @param parent   The parent layer
     * @param rootNode The node for that layer
     */
    private void createBach(JTabbedPane parent, TreeNode<Class<? extends DataObject>> rootNode) {
        if (rootNode.children.size() == 0) {

            // Build the bottom of the tree, the actual object
            DataObject_VerbosityDisplayList<?> list = new DataObject_VerbosityDisplayList<>(database, rootNode.data, this);
            parent.add(rootNode.data.getSimpleName(), list);
            updatableList.add(list);
        } else {
            if (rootNode.data.equals(NamedDataObject.class)) {

                // Treat NamedDataObject as past of the parent layer
                for (TreeNode<Class<? extends DataObject>> node : rootNode.children) {
                    createBach(parent, node);
                }
            } else {

                // Build a new layer
                JTabbedPane container = new JTabbedPane();
                parent.add(rootNode.data.getSimpleName(), container);

                // If this is a solid object display it as well
                if (!Modifier.isAbstract(rootNode.data.getModifiers())) {
                    if (!PoolSummary.class.isAssignableFrom(rootNode.data) && !Period_Summary.class.isAssignableFrom(rootNode.data)) {
                        DataObject_VerbosityDisplayList<?> list = new DataObject_VerbosityDisplayList<>(database, rootNode.data, this);
                        container.add(rootNode.data.getSimpleName(), list);
                        updatableList.add(list);
                    }
                }

                for (TreeNode<Class<? extends DataObject>> node : rootNode.children) {
                    createBach(container, node);
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        if (database.getClassTreeRoot().size() != objectTypeSize) {
            rootTabbedPane.removeAll();
            updatableList.clear();
            createBach(rootTabbedPane, database.getClassTreeRoot());
            objectTypeSize = database.getClassTreeRoot().size();
        }
        updatableList.forEach(DataObject_VerbosityDisplayList::update);
    }
}
