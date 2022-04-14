package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.Translation;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TranslationTypes;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;

public class Translation_ElementController extends Database_ElementController<Translation> {

    /**
     * Constructor
     */
    public Translation_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Translation newElement() {
        return new Translation(getTrackingDatabase(), "", "", getTrackingDatabase().getDefault(TranslationTypes.class));
    }
}
