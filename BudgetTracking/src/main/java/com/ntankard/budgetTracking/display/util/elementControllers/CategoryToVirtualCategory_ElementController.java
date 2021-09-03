package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToVirtualCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.category.VirtualCategory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;

import java.util.List;

public class CategoryToVirtualCategory_ElementController extends Database_ElementController<CategoryToVirtualCategory> {

    /**
     * Data to use when creating a new object
     */
    private VirtualCategory virtualCategory;

    /**
     * Constructor
     */
    public CategoryToVirtualCategory_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public CategoryToVirtualCategory newElement() {
        if (virtualCategory == null) {
            throw new RuntimeException("Creating an object without a VirtualCategory being provided");
        }
        List<SolidCategory> options = virtualCategory.getTrackingDatabase().get(SolidCategory.class);
        options.removeAll(virtualCategory.getCategorySet().getUsedCategories());
        if (options.size() == 0) {
            throw new RuntimeException("Can not create");
        }

        return new CategoryToVirtualCategory(
                virtualCategory,
                options.get(0));
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
     * @inheritDoc
     */
    @Override
    public boolean canCreate() {
        if (virtualCategory == null) {
            return false;
        }
        List<SolidCategory> options = virtualCategory.getTrackingDatabase().get(SolidCategory.class);
        options.removeAll(virtualCategory.getCategorySet().getUsedCategories());
        return options.size() != 0;
    }
}
