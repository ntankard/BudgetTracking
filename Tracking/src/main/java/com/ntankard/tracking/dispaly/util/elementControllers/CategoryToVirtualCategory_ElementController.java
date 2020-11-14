package com.ntankard.tracking.dispaly.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.tracking.dataBase.core.links.CategoryToVirtualCategory;
import com.ntankard.tracking.dataBase.core.pool.category.VirtualCategory;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dispaly.util.panels.TrackingDatabase_ElementController;

public class CategoryToVirtualCategory_ElementController extends TrackingDatabase_ElementController<CategoryToVirtualCategory> {

    /**
     * Data to use when creating a new object
     */
    private VirtualCategory virtualCategory;

    /**
     * Constructor
     */
    public CategoryToVirtualCategory_ElementController(TrackingDatabase trackingDatabase, Updatable master) {
        super(trackingDatabase, master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryToVirtualCategory newElement() {
        if (virtualCategory == null) {
            throw new RuntimeException("Creating an object without a VirtualCategory being provided");
        }
        return CategoryToVirtualCategory.make(getTrackingDatabase().getNextId(),
                virtualCategory,
                virtualCategory.getCategorySet().getAvailableCategories().get(0));
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param virtualCategory The data to use when creating a new object
     */
    public void setVirtualCategory(VirtualCategory virtualCategory) {
        this.virtualCategory = virtualCategory;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canCreate() {
        return virtualCategory != null && virtualCategory.getCategorySet().getAvailableCategories().size() != 0;
    }
}
