package com.ntankard.tracking.dataBase.core.links;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
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
        DataObject_Schema dataObjectSchema = DataObject.getDataObjectSchema();

        // ID
        // VirtualCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_VirtualCategory, VirtualCategory.class));
        // SolidCategory ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_SolidCategory, SolidCategory.class));
        dataObjectSchema.get(CategoryToVirtualCategory_SolidCategory).setManualCanEdit(true);
        dataObjectSchema.<SolidCategory>get(CategoryToVirtualCategory_SolidCategory).addFilter(new Dependant_FieldFilter<SolidCategory, CategoryToVirtualCategory>(CategoryToVirtualCategory_VirtualCategory) {
            @Override
            public boolean isValid(SolidCategory newValue, SolidCategory pastValue, CategoryToVirtualCategory categoryToVirtualCategory) {
                return !categoryToVirtualCategory.getVirtualCategory().getCategorySet().getUsedCategories().contains(newValue) || newValue.equals(categoryToVirtualCategory.getSolidCategory());
            }
        });
        // CategorySet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToVirtualCategory_CategorySet, CategorySet.class));
        dataObjectSchema.get(CategoryToVirtualCategory_CategorySet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternal_Source.DirectExternalSource_Factory<>(
                                (CategoryToVirtualCategory_VirtualCategory), VirtualCategory_CategorySet)));
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
     * {@inheritDoc
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
     * {@inheritDoc
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
