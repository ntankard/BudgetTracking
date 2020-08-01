package com.ntankard.Tracking.DataBase.Core.Links;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;

import java.util.List;

public class CategoryToVirtualCategory extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategoryToVirtualCategory_VirtualCategory = "getVirtualCategory";
    public static final String CategoryToVirtualCategory_SolidCategory = "getSolidCategory";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // VirtualCategory =============================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToVirtualCategory_VirtualCategory, VirtualCategory.class));
        // SolidCategory ===============================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToVirtualCategory_SolidCategory, SolidCategory.class));
        fieldContainer.<SolidCategory>get(CategoryToVirtualCategory_SolidCategory).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.<SolidCategory>get(CategoryToVirtualCategory_SolidCategory).addFilter(new Dependant_FieldFilter<SolidCategory, CategoryToVirtualCategory>(CategoryToVirtualCategory_VirtualCategory) {
            @Override
            public boolean isValid(SolidCategory value, CategoryToVirtualCategory categoryToVirtualCategory) {
                if (value == null)
                    return false;
                return !categoryToVirtualCategory.getVirtualCategory().getCategorySet().getUsedCategories().contains(value) || value.equals(categoryToVirtualCategory.getSolidCategory());
            }
        });
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(CategoryToVirtualCategory.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategoryToVirtualCategory make(Integer id, VirtualCategory virtualCategory, SolidCategory solidCategory) {
        return assembleDataObject(CategoryToVirtualCategory.getFieldContainer(), new CategoryToVirtualCategory()
                , DataObject_Id, id
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
