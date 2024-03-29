package com.ntankard.budgetTracking.dataBase.core.links;

import com.ntankard.budgetTracking.dataBase.core.CategorySet;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.category.VirtualCategory;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.validator.shared.Multi_FieldValidator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.pool.category.VirtualCategory.VirtualCategory_CategorySet;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;

public class CategoryToVirtualCategory extends DataObject {

    public interface CategoryToVirtualCategoryList extends List<CategoryToVirtualCategory> {
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategoryToVirtualCategory_VirtualCategory = "getVirtualCategory";
    public static final String CategoryToVirtualCategory_SolidCategory = "getSolidCategory";
    public static final String CategoryToVirtualCategory_CategorySet = "getCategorySet";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        Multi_FieldValidator<CategoryToVirtualCategory> sharedFilter = new Multi_FieldValidator<>(
                (newValues, pastValues, container) ->
                        !((VirtualCategory) newValues[1]).getCategorySet().getUsedCategories().contains(((SolidCategory) newValues[0])) || newValues[0].equals(container.getSolidCategory()),
                "CategoryToVirtualCategory", CategoryToVirtualCategory_SolidCategory, CategoryToVirtualCategory_VirtualCategory);

        // ID
        // VirtualCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_VirtualCategory, VirtualCategory.class));
        dataObjectSchema.get(CategoryToVirtualCategory_VirtualCategory).addValidator(sharedFilter.getValidator(CategoryToVirtualCategory_VirtualCategory));
        // SolidCategory ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_SolidCategory, SolidCategory.class));
        dataObjectSchema.get(CategoryToVirtualCategory_SolidCategory).setManualCanEdit(true);
        dataObjectSchema.get(CategoryToVirtualCategory_SolidCategory).addValidator(sharedFilter.getValidator(CategoryToVirtualCategory_SolidCategory));
        // CategorySet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_CategorySet, CategorySet.class));
        dataObjectSchema.<CategorySet>get(CategoryToVirtualCategory_CategorySet).setDataCore_schema(createDirectDerivedDataCore(CategoryToVirtualCategory_VirtualCategory, VirtualCategory_CategorySet));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategoryToVirtualCategory.class);
    }

    /**
     * Constructor
     */
    public CategoryToVirtualCategory(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public CategoryToVirtualCategory(VirtualCategory virtualCategory, SolidCategory solidCategory) {
        super(virtualCategory.getTrackingDatabase()
                , CategoryToVirtualCategory_VirtualCategory, virtualCategory
                , CategoryToVirtualCategory_SolidCategory, solidCategory
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("SolidCategory")) {
            List<T> toReturn = super.sourceOptions(type, fieldName);
            toReturn.removeAll(getVirtualCategory().getCategorySet().getUsedCategories());
            toReturn.add((T) getSolidCategory());
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
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

    public VirtualCategory getVirtualCategory() {
        return get(CategoryToVirtualCategory_VirtualCategory);
    }

    public SolidCategory getSolidCategory() {
        return get(CategoryToVirtualCategory_SolidCategory);
    }
}
