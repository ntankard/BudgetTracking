package com.ntankard.Tracking.DataBase.Core.Links;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategoryToCategorySet extends DataObject implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new DataObject_Field<>("getCategorySet", CategorySet.class));
        toReturn.add(new DataObject_Field<>("getSolidCategory", SolidCategory.class));
        toReturn.add(new Field<>("getOrderImpl", Integer.class));
        return toReturn;
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategoryToCategorySet make(Integer id, CategorySet categorySet, SolidCategory solidCategory, Integer orderImpl) {
        return assembleDataObject(CategoryToCategorySet.getFields(), new CategoryToCategorySet()
                , "getId", id
                , "getCategorySet", categorySet
                , "getSolidCategory", solidCategory
                , "getOrderImpl", orderImpl
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("SolidCategory")) {
            List<T> toReturn = (List<T>) getCategorySet().getAvailableCategories();
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
    public CategorySet getCategorySet() {
        return get("getCategorySet");
    }

    @DisplayProperties(order = 1200000)
    public SolidCategory getSolidCategory() {
        return get("getSolidCategory");
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1300000)
    public Integer getOrderImpl() {
        return get("getOrderImpl");
    }

    @Override
    @DisplayProperties(order = 1400000)
    public Integer getOrder() {
        return getCategorySet().getOrder() * 1000 + getOrderImpl();
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSolidCategory(SolidCategory solidCategory) {
        if (solidCategory == null) throw new IllegalArgumentException("solidCategory is null");
        if (getCategorySet().getUsedCategories().contains(solidCategory) && !solidCategory.equals(getSolidCategory()))
            throw new IllegalArgumentException("solidCategory used in this set already");
        set("getSolidCategory", solidCategory);
        validateParents();
    }

    public void setOrderImpl(Integer orderImpl) {
        if (orderImpl == null) throw new IllegalArgumentException("orderImpl is null");
        set("getOrderImpl", orderImpl);
    }
}