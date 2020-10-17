package com.ntankard.Tracking.DataBase.Core.Links;

import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.CategorySet;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;

import java.util.List;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
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
        fieldContainer.add(new DataField<>(CategoryToCategorySet_CategorySet, CategorySet.class));
        // SolidCategory ========================================================================================================
        fieldContainer.add(new DataField<>(CategoryToCategorySet_SolidCategory, SolidCategory.class));
        fieldContainer.get(CategoryToCategorySet_SolidCategory).setManualCanEdit(true);
        fieldContainer.<SolidCategory>get(CategoryToCategorySet_SolidCategory).addFilter(new Dependant_FieldFilter<SolidCategory, CategoryToCategorySet>(CategoryToCategorySet_CategorySet) {
            @Override
            public boolean isValid(SolidCategory newValue, SolidCategory pastValue, CategoryToCategorySet categoryToCategorySet) {
                return !categoryToCategorySet.getCategorySet().getUsedCategories().contains(newValue) || newValue.equals(categoryToCategorySet.getSolidCategory());
            }
        });
        // OrderImpl ========================================================================================================
        fieldContainer.add(new DataField<>(CategoryToCategorySet_OrderImpl, Integer.class));
        fieldContainer.get(CategoryToCategorySet_OrderImpl).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(CategoryToCategorySet_OrderImpl).setManualCanEdit(true);
        // Order ========================================================================================================
        fieldContainer.add(new DataField<>(CategoryToCategorySet_Order, Integer.class));
        fieldContainer.get(CategoryToCategorySet_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((CategoryToCategorySet) container).getCategorySet().getOrder() * 1000 + ((CategoryToCategorySet) container).getOrderImpl()
                                , new LocalSource.LocalSource_Factory<>((CategoryToCategorySet_OrderImpl))
                                , new ExternalSource.ExternalSource_Factory<>((CategoryToCategorySet_CategorySet), CategorySet_Order)));
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