package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_VerbosityDisplayList;
import com.ntankard.Tracking.Util.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Modifier;

public class DatabasePanel extends UpdatableJPanel {

    // The GUI components
    private JTabbedPane rootTabbedPane;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected DatabasePanel(Updatable master) {
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
            DataObject_VerbosityDisplayList list = new DataObject_VerbosityDisplayList<>(rootNode.data, this);
            parent.add(rootNode.data.getSimpleName(), list);
            list.update();
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
                    DataObject_VerbosityDisplayList list = new DataObject_VerbosityDisplayList<>(rootNode.data, this);
                    container.add(rootNode.data.getSimpleName(), list);
                    list.update();
                }

                for (TreeNode<Class<? extends DataObject>> node : rootNode.children) {
                    createBach(container, node);
                }
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        rootTabbedPane.removeAll();
        createBach(rootTabbedPane, TrackingDatabase.get().getClassTreeRoot());
    }
}
