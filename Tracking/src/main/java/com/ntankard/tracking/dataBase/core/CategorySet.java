package com.ntankard.tracking.dataBase.core;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.EndSource_Schema;
import com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.tracking.dataBase.core.links.CategoryToCategorySet;
import com.ntankard.tracking.dataBase.core.links.CategoryToVirtualCategory;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.category.VirtualCategory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.HasDefault;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DEBUG_DISPLAY;

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
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Default ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Default, Boolean.class));
        dataObjectSchema.get(CategorySet_Default).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_Order, Integer.class));
        dataObjectSchema.get(CategorySet_Order).setManualCanEdit(true);
        // UsedCategories ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_UsedCategories, List.class)); // TODO this should be a list field
        dataObjectSchema.get(CategorySet_UsedCategories).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.<List<SolidCategory>>get(CategorySet_UsedCategories).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
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
                        , new EndSource_Schema<>(DataObject_ChildrenField)));
        // AvailableCategories =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategorySet_AvailableCategories, List.class));
        dataObjectSchema.<List<SolidCategory>>get(CategorySet_AvailableCategories).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<List<SolidCategory>, CategorySet>) container -> {
                            List<SolidCategory> toReturn = container.getTrackingDatabase().get(SolidCategory.class); // TODO this is broken, you need to get alerts about this being updated
                            toReturn.removeAll(container.getUsedCategories());
                            return toReturn;
                        }
                        , new EndSource_Schema<>(CategorySet_UsedCategories)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategorySet.class);
    }

    /**
     * Constructor
     */
    public CategorySet(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public CategorySet(Database database, String name, Boolean isDefault, Integer order) {
        this(database);
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , NamedDataObject_Name, name
                , CategorySet_Default, isDefault
                , CategorySet_Order, order
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    private List<SolidCategory> getAvailableCategoriesImpl() {
        List<SolidCategory> toReturn = getTrackingDatabase().get(SolidCategory.class);
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
