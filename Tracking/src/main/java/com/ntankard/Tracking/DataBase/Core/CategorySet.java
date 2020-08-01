package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;

public class CategorySet extends NamedDataObject implements HasDefault, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategorySet_Default = "isDefault";
    public static final String CategorySet_Order = "getOrder";
    public static final String CategorySet_AvailableCategories = "getAvailableCategories";
    public static final String CategorySet_UsedCategories = "getUsedCategories";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Default ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategorySet_Default, Boolean.class));
        fieldContainer.get(CategorySet_Default).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategorySet_Order, Integer.class));
        fieldContainer.get(CategorySet_Order).setDataCore(new ValueRead_DataCore<>(true));
        // AvailableCategories =========================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategorySet_AvailableCategories, List.class));
        fieldContainer.get(CategorySet_AvailableCategories).setDataCore(new Method_DataCore<>(container -> ((CategorySet) container).getAvailableCategoriesImpl()));
        // UsedCategories ==============================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategorySet_UsedCategories, List.class));
        fieldContainer.get(CategorySet_UsedCategories).setDataCore(new Method_DataCore<>(container -> ((CategorySet) container).getUsedCategoriesImpl()));
        fieldContainer.get(CategorySet_UsedCategories).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(CategorySet.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategorySet make(Integer id, String name, Boolean isDefault, Integer order) {
        return assembleDataObject(CategorySet.getFieldContainer(), new CategorySet()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , CategorySet_Default, isDefault
                , CategorySet_Order, order
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

    private List<SolidCategory> getAvailableCategoriesImpl() {
        List<SolidCategory> toReturn = TrackingDatabase.get().get(SolidCategory.class);
        toReturn.removeAll(getUsedCategories());
        return toReturn;
    }

    private List<SolidCategory> getUsedCategoriesImpl() {
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

    @Override
    public Boolean isDefault() {
        return get(CategorySet_Default);
    }

    @Override
    public Integer getOrder() {
        return get(CategorySet_Order);
    }

    public List<SolidCategory> getAvailableCategories() {
        return get(CategorySet_AvailableCategories);
    }

    public List<SolidCategory> getUsedCategories() {
        return get(CategorySet_UsedCategories);
    }
}
