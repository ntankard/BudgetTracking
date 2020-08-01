package com.ntankard.Tracking.DataBase.Core.Links;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;

import java.util.List;

import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.Tracking.DataBase.Core.CategorySet.CategorySet_Order;

public class CategoryToCategorySet extends DataObject implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String CategoryToCategorySet_CategorySet = "getCategorySet";
    public static final String CategoryToCategorySet_SolidCategory = "getSolidCategory";
    public static final String CategoryToCategorySet_OrderImpl = "getOrderImpl";
    public static final String CategoryToCategorySet_Order = "getOrder";


    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // CategorySet ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToCategorySet_CategorySet, CategorySet.class));
        // SolidCategory ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToCategorySet_SolidCategory, SolidCategory.class));
        fieldContainer.<SolidCategory>get(CategoryToCategorySet_SolidCategory).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.<SolidCategory>get(CategoryToCategorySet_SolidCategory).addFilter(new Dependant_FieldFilter<SolidCategory, CategoryToCategorySet>(CategoryToCategorySet_CategorySet) {
            @Override
            public boolean isValid(SolidCategory value, CategoryToCategorySet categoryToCategorySet) {
                return !categoryToCategorySet.getCategorySet().getUsedCategories().contains(value) || value.equals(categoryToCategorySet.getSolidCategory());
            }
        });
        // OrderImpl ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToCategorySet_OrderImpl, Integer.class));
        fieldContainer.get(CategoryToCategorySet_OrderImpl).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(CategoryToCategorySet_OrderImpl).setDataCore(new ValueRead_DataCore<>(true));
        // Order ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(CategoryToCategorySet_Order, Integer.class));
        fieldContainer.get(CategoryToCategorySet_Order).setDataCore(
                new Derived_DataCore<>
                        (container -> ((CategoryToCategorySet) container).getCategorySet().getOrder() * 1000 + ((CategoryToCategorySet) container).getOrderImpl()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(CategoryToCategorySet_OrderImpl))
                                , new Derived_DataCore.ExternalSource<>(fieldContainer.get(CategoryToCategorySet_CategorySet), CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(CategoryToCategorySet.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static CategoryToCategorySet make(Integer id, CategorySet categorySet, SolidCategory solidCategory, Integer orderImpl) {
        return assembleDataObject(CategoryToCategorySet.getFieldContainer(), new CategoryToCategorySet()
                , DataObject_Id, id
                , CategoryToCategorySet_CategorySet, categorySet
                , CategoryToCategorySet_SolidCategory, solidCategory
                , CategoryToCategorySet_OrderImpl, orderImpl
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

    public CategorySet getCategorySet() {
        return get(CategoryToCategorySet_CategorySet);
    }

    public SolidCategory getSolidCategory() {
        return get(CategoryToCategorySet_SolidCategory);
    }

    public Integer getOrderImpl() {
        return get(CategoryToCategorySet_OrderImpl);
    }

    @Override
    public Integer getOrder() {
        return get(CategoryToCategorySet_Order);
    }
}