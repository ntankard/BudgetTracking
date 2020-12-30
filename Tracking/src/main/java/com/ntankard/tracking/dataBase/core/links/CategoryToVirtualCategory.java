package com.ntankard.tracking.dataBase.core.links;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.validator.Shared_FieldValidator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.category.VirtualCategory;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.pool.category.VirtualCategory.VirtualCategory_CategorySet;

public class CategoryToVirtualCategory extends DataObject {

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

        Shared_FieldValidator<SolidCategory, VirtualCategory, CategoryToVirtualCategory> sharedFilter = new Shared_FieldValidator<>(CategoryToVirtualCategory_SolidCategory, CategoryToVirtualCategory_VirtualCategory,
                (firstNewValue, secondNewValue, container) ->
                        !secondNewValue.getCategorySet().getUsedCategories().contains(firstNewValue) || firstNewValue.equals(container.getSolidCategory()));

        // ID
        // VirtualCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_VirtualCategory, VirtualCategory.class));
        dataObjectSchema.<VirtualCategory>get(CategoryToVirtualCategory_VirtualCategory).addValidator(sharedFilter.getSecondFilter());
        // SolidCategory ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_SolidCategory, SolidCategory.class));
        dataObjectSchema.get(CategoryToVirtualCategory_SolidCategory).setManualCanEdit(true);
        dataObjectSchema.<SolidCategory>get(CategoryToVirtualCategory_SolidCategory).addValidator(sharedFilter.getFirstFilter());
        // CategorySet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_CategorySet, CategorySet.class));
        dataObjectSchema.<CategorySet>get(CategoryToVirtualCategory_CategorySet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<CategorySet, CategoryToVirtualCategory>
                        (dataObject -> dataObject.getVirtualCategory().getCategorySet()
                                , Source_Factory.makeSourceChain(CategoryToVirtualCategory_VirtualCategory, VirtualCategory_CategorySet)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategoryToVirtualCategory.class);
    }

    /**
     * Constructor
     */
    public CategoryToVirtualCategory(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public CategoryToVirtualCategory(VirtualCategory virtualCategory, SolidCategory solidCategory) {
        this(virtualCategory.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , CategoryToVirtualCategory_VirtualCategory, virtualCategory
                , CategoryToVirtualCategory_SolidCategory, solidCategory
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("SolidCategory")) {
            List<T> toReturn = (List<T>) getVirtualCategory().getCategorySet().getAvailableCategories();
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
