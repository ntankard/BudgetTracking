package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategorySet extends NamedDataObject implements HasDefault, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = NamedDataObject.getFields();
        toReturn.add(new Field<>("isDefault", Boolean.class));
        toReturn.add(new Field<>("getOrder", Integer.class));
        return toReturn;
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategorySet make(Integer id, String name, Boolean isDefault, Integer order) {
        return assembleDataObject(CategorySet.getFields(), new CategorySet()
                , "getId", id
                , "getName", name
                , "isDefault", isDefault
                , "getOrder", order
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
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 1130000)
    public List<SolidCategory> getAvailableCategories() {
        List<SolidCategory> toReturn = TrackingDatabase.get().get(SolidCategory.class);
        toReturn.removeAll(getUsedCategories());
        return toReturn;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1140000)
    public List<SolidCategory> getUsedCategories() {
        List<SolidCategory> toReturn = new ArrayList<>();
        for (CategoryToCategorySet categoryToCategorySet : getChildren(CategoryToCategorySet.class)) {
            toReturn.add(categoryToCategorySet.getSolidCategory());
        }
        for (VirtualCategory virtualCategory : getChildren(VirtualCategory.class)) {
            for (CategoryToVirtualCategory categoryToVirtualCategory : virtualCategory.getChildren(CategoryToVirtualCategory.class)) {
                toReturn.add(categoryToVirtualCategory.getSolidCategory());
            }
        }
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1110000)
    public Boolean isDefault() {
        return get("isDefault");
    }

    @Override
    @DisplayProperties(order = 1120000)
    public Integer getOrder() {
        return get("getOrder");
    }

    // 1130000------getAvailableCategories(above)
    // 1140000------getUsedCategories (above)
    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setOrder(Integer order) {
        set("getOrder", order);
    }
}
