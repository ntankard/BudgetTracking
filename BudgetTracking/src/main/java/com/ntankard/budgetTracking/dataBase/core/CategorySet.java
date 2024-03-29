package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.budgetTracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToCategorySet;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToCategorySet.CategoryToCategorySetList;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToVirtualCategory;
import com.ntankard.budgetTracking.dataBase.core.links.CategoryToVirtualCategory.CategoryToVirtualCategoryList;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.links.CategoryToCategorySet.CategoryToCategorySet_SolidCategory;
import static com.ntankard.budgetTracking.dataBase.core.links.CategoryToVirtualCategory.CategoryToVirtualCategory_SolidCategory;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class CategorySet extends NamedDataObject implements Ordered {

    public interface SolidCategoryList extends List<SolidCategory> {
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategorySet_Default = "isDefault";
    public static final String CategorySet_Order = "getOrder";

    public static final String CategorySet_CategoryToCategorySetList = "getCategoryToCategorySetList";
    public static final String CategorySet_CategoryToVirtualCategoryList = "getCategoryToVirtualCategoryList";
    public static final String CategorySet_UsedCategories = "getUsedCategories";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Default ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Default, Boolean.class));
        dataObjectSchema.get(CategorySet_Default).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(CategorySet_Default).setDefaultFlag(true);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Order, Integer.class));
        dataObjectSchema.get(CategorySet_Order).setManualCanEdit(true);
        // CategoryToCategorySetList ===================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(CategorySet_CategoryToCategorySetList, CategoryToCategorySetList.class));
        dataObjectSchema.<List<CategoryToCategorySet>>get(CategorySet_CategoryToCategorySetList).setDataCore_schema(
                createSelfParentList(CategoryToCategorySet.class, null));
        // CategoryToVirtualCategoryList ===============================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(CategorySet_CategoryToVirtualCategoryList, CategoryToVirtualCategoryList.class));
        dataObjectSchema.<List<CategoryToVirtualCategory>>get(CategorySet_CategoryToVirtualCategoryList).setDataCore_schema(
                createSelfParentList(CategoryToVirtualCategory.class, null));
        // UsedCategories ==============================================================================================
        // CategorySet_UsedCategories and CategorySet_AvailableCategories are wrong, have seen a case in the set test where the same value is in both
        dataObjectSchema.add(new ListDataField_Schema<>(CategorySet_UsedCategories, SolidCategoryList.class));
        dataObjectSchema.get(CategorySet_UsedCategories).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.<List<SolidCategory>>get(CategorySet_UsedCategories).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<List<SolidCategory>, CategorySet>) container -> {
                            List<SolidCategory> toReturn = new ArrayList<>();
                            for (CategoryToCategorySet categoryToCategorySet : container.getChildren(CategoryToCategorySet.class)) {
                                toReturn.add(categoryToCategorySet.getSolidCategory());
                            }

                            for (CategoryToVirtualCategory categoryToVirtualCategory : container.getChildren(CategoryToVirtualCategory.class)) {
                                toReturn.add(categoryToVirtualCategory.getSolidCategory());
                            }
                            return toReturn;
                        }
                        , makeSourceChain(CategorySet_CategoryToCategorySetList, CategoryToCategorySet_SolidCategory)
                        , makeSourceChain(CategorySet_CategoryToVirtualCategoryList, CategoryToVirtualCategory_SolidCategory)));
        // TODO change to new method
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategorySet.class);
    }

    /**
     * Constructor
     */
    public CategorySet(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public CategorySet(Database database, String name, Boolean isDefault, Integer order) {
        super(database
                , NamedDataObject_Name, name
                , CategorySet_Default, isDefault
                , CategorySet_Order, order
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Boolean isDefault() {
        return get(CategorySet_Default);
    }

    @Override
    public Integer getOrder() {
        return get(CategorySet_Order);
    }

    public List<SolidCategory> getUsedCategories() {
        return get(CategorySet_UsedCategories);
    }
}
