package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategoryToVirtualCategory_ElementController extends TrackingDatabase_ElementController<CategoryToVirtualCategory> {

    /**
     * Data to use when creating a new object
     */
    private VirtualCategory virtualCategory;

    /**
     * Constructor
     */
    public CategoryToVirtualCategory_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryToVirtualCategory newElement() {
        if (virtualCategory == null) {
            throw new RuntimeException("Creating an object without a VirtualCategory being provided");
        }
        return CategoryToVirtualCategory.make(TrackingDatabase.get().getNextId(),
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
