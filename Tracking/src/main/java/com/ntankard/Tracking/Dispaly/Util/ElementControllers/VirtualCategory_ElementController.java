package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class VirtualCategory_ElementController extends TrackingDatabase_ElementController<VirtualCategory> {

    /**
     * Data to use when creating a new object
     */
    private CategorySet categorySet;

    /**
     * Constructor
     */
    public VirtualCategory_ElementController(Updatable master) {
        super(master);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public VirtualCategory newElement() {
        if (categorySet == null) {
            throw new RuntimeException("Creating an object without a CategorySet being provided");
        }
        return VirtualCategory.make(TrackingDatabase.get().getNextId(),
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
