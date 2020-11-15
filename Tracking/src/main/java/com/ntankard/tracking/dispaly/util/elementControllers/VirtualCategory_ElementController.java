package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.tracking.dataBase.core.pool.category.VirtualCategory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class VirtualCategory_ElementController extends TrackingDatabase_ElementController<VirtualCategory> {

    /**
     * Data to use when creating a new object
     */
    private CategorySet categorySet;

    /**
     * Constructor
     */
    public VirtualCategory_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public VirtualCategory newElement() {
        if (categorySet == null) {
            throw new RuntimeException("Creating an object without a CategorySet being provided");
        }
        return new VirtualCategory(
                "",
                categorySet,
                0);
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param categorySet The data to use when creating a new object
     */
    public void setCategorySet(CategorySet categorySet) {
        this.categorySet = categorySet;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canCreate() {
        return categorySet != null;
    }
}
