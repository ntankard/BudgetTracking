package com.ntankard.budgetTracking.display.frames.mainFrame.statement;

import com.ntankard.budgetTracking.display.frames.mainFrame.statement.group.GroupPanel;
import com.ntankard.budgetTracking.display.frames.mainFrame.statement.group.TranslationPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;

import javax.swing.*;
import java.awt.*;

public class StatementPanel extends UpdatableJPanel {

    // The GUI components
    private FileCheckPanel fileCheckPanel;
    private GroupPanel groupPanel;
    private TranslationPanel translationPanel;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public StatementPanel(Database database, Updatable master) {
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

        fileCheckPanel = new FileCheckPanel(database, this);
        translationPanel = new TranslationPanel(database,this);
        groupPanel = new GroupPanel(database, this);

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("File", fileCheckPanel);
        master_tPanel.addTab("Translation", translationPanel);
        master_tPanel.addTab("Group", groupPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        fileCheckPanel.update();
        translationPanel.update();
        groupPanel.update();
    }
}
