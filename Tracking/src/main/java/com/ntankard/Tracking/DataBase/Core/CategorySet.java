package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.VirtualCategory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.SelfChild;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Default ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Default, Boolean.class));
        dataObjectSchema.get(CategorySet_Default).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Order, Integer.class));
        dataObjectSchema.get(CategorySet_Order).setManualCanEdit(true);
        // UsedCategories ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_UsedCategories, List.class)); // TODO this should be a list field
        dataObjectSchema.get(CategorySet_UsedCategories).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.<List<SolidCategory>>get(CategorySet_UsedCategories).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<List<SolidCategory>, CategorySet>) container -> {
                            List<SolidCategory> toReturn = new ArrayList<>();
                            for (CategoryToCategorySet categoryToCategorySet : container.getChildren(CategoryToCategorySet.class)) {
                                toReturn.add(categoryToCategorySet.getSolidCategory()); // TODO should have this as a set and subscribe to the set
                            }

                            for (CategoryToVirtualCategory categoryToVirtualCategory : container.getChildren(CategoryToVirtualCategory.class)) {
                                toReturn.add(categoryToVirtualCategory.getSolidCategory()); // TODO should have this as a set and subscribe to the set
                            }
                            return toReturn;
                        }
                        , new SelfChild.SelfChildSource_Factory<>()));
        // AvailableCategories =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_AvailableCategories, List.class));
        dataObjectSchema.<List<SolidCategory>>get(CategorySet_AvailableCategories).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<List<SolidCategory>, CategorySet>) container -> {
                            List<SolidCategory> toReturn = TrackingDatabase.get().get(SolidCategory.class); // TODO this is broken, you need to get alerts about this being updated
                            toReturn.removeAll(container.getUsedCategories());
                            return toReturn;
                        }
                        , new LocalSource.LocalSource_Factory<>(CategorySet_UsedCategories)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategorySet.class);
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
