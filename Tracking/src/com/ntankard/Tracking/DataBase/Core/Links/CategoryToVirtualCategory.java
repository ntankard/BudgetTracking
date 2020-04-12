package com.ntankard.Tracking.DataBase.Core.Links;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class CategoryToVirtualCategory extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new DataObject_Field<>("getVirtualCategory", VirtualCategory.class));
        toReturn.add(new DataObject_Field<>("getSolidCategory", SolidCategory.class));
        return toReturn;
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategoryToVirtualCategory make(Integer id, VirtualCategory virtualCategory, SolidCategory solidCategory) {
        return assembleDataObject(CategoryToVirtualCategory.getFields(), new CategoryToVirtualCategory()
                , "getId", id
                , "getVirtualCategory", virtualCategory
                , "getSolidCategory", solidCategory
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

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public VirtualCategory getVirtualCategory() {
        return get("getVirtualCategory");
    }

    @DisplayProperties(order = 1200000)
    public SolidCategory getSolidCategory() {
        return get("getSolidCategory");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSolidCategory(SolidCategory solidCategory) {
        if (solidCategory == null) throw new IllegalArgumentException("solidCategory is null");
        if (getVirtualCategory().getCategorySet().getUsedCategories().contains(solidCategory) && !solidCategory.equals(getSolidCategory()))
            throw new IllegalArgumentException("solidCategory used in this set already");
        set("getSolidCategory", solidCategory);
        validateParents();
    }
}
