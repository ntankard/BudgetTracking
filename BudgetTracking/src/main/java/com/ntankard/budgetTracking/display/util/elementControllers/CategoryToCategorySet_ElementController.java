package com.ntankard.budgetTracking.display.util.elementControllers;

import com.ntankard.budgetTracking.dataBase.core.CategorySet;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToCategorySet;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.dynamicGUI.gui.containers.Database_ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

public class CategoryToCategorySet_ElementController extends Database_ElementController<CategoryToCategorySet> {

    /**
     * Data to use when creating a new object
     */
    private CategorySet categorySet;

    /**
     * Constructor
     */
    public CategoryToCategorySet_ElementController(Database database, Updatable master) {
        super(database, master);
    }

    /**
     * @inheritDoc
     */
    @Override
    public CategoryToCategorySet newElement() {
        if (categorySet == null) {
            throw new RuntimeException("Creating an object without a VirtualCategory being provided");
        }

        List<SolidCategory> options = categorySet.getTrackingDatabase().get(SolidCategory.class);
        options.removeAll(categorySet.getUsedCategories());
        if (options.size() == 0) {
            throw new RuntimeException("Can not create");
        }

        return new CategoryToCategorySet(
                categorySet,
                options.get(0),
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
     * @inheritDoc
     */
    @Override
    public boolean canCreate() {
        if (categorySet == null) {
            return false;
        }
        List<SolidCategory> options = categorySet.getTrackingDatabase().get(SolidCategory.class);
        options.removeAll(categorySet.getUsedCategories());
        return options.size() != 0;
    }
}
