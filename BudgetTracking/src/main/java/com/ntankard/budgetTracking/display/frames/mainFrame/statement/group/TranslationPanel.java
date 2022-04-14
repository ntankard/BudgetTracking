package com.ntankard.budgetTracking.display.frames.mainFrame.statement.group;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation;
import com.ntankard.budgetTracking.display.util.elementControllers.Translation_ElementController;
import com.ntankard.budgetTracking.display.util.panels.DataObject_DisplayList;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Full_Set;

import java.awt.*;

public class TranslationPanel extends UpdatableJPanel {

    // The GUI components
    private DataObject_DisplayList<Translation> translation_panel;

    // Core database
    private final Database database;

    /**
     * Constructor
     */
    public TranslationPanel(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        translation_panel = new DataObject_DisplayList<>(database.getSchema(), Translation.class, new Full_Set<>(database, Translation.class), false, this);
        translation_panel.addControlButtons(new Translation_ElementController(database, this));

        GridBagConstraints summaryContainer_C = new GridBagConstraints();
        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;
        summaryContainer_C.weighty = 1;

        summaryContainer_C.gridx = 0;
        this.add(translation_panel, summaryContainer_C);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        translation_panel.update();
    }
}
