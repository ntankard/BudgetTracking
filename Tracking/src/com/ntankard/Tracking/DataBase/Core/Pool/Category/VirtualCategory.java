package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.CategorySet;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class VirtualCategory extends Category implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = Category.getFields();
        toReturn.add(new DataObject_Field<>("getCategorySet", CategorySet.class));
        toReturn.add(new Field<>("getOrderImpl", Integer.class));
        return toReturn;
    }

    /**
     * Create a new VirtualCategory object
     */
    public static VirtualCategory make(Integer id, String name, CategorySet categorySet, Integer orderImpl) {
        return assembleDataObject(VirtualCategory.getFields(), new VirtualCategory()
                , "getId", id
                , "getName", name
                , "getCategorySet", categorySet
                , "getOrderImpl", orderImpl
        );
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
    // 1100000----getName

    @DisplayProperties(order = 1100100)
    public CategorySet getCategorySet() {
        return get("getCategorySet");
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1100200)
    public Integer getOrderImpl() {
        return get("getOrderImpl");
    }

    @Override
    @DisplayProperties(order = 1100300)
    public Integer getOrder() {
        return getCategorySet().getOrder() * 1000 + getOrderImpl();
    }

    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setOrderImpl(Integer orderImpl) {
        if (orderImpl == null) throw new IllegalArgumentException("orderImpl is null");
        set("getOrderImpl", orderImpl);
    }
}
