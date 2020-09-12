package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class CategoryToCategorySet_ElementController extends TrackingDatabase_ElementController<CategoryToCategorySet> {

    /**
     * Data to use when creating a new object
     */
    private CategorySet categorySet;

    /**
     * Constructor
     */
    public CategoryToCategorySet_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryToCategorySet newElement() {
        if (categorySet == null) {
            throw new RuntimeException("Creating an object without a VirtualCategory being provided");
        }
        return CategoryToCategorySet.make(TrackingDatabase.get().getNextId(),
                categorySet,
                categorySet.getAvailableCategories().get(0),
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
        return categorySet != null && categorySet.getAvailableCategories().size() != 0;
    }
}
