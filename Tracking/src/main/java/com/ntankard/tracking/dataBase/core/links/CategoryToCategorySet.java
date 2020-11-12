package com.ntankard.tracking.dataBase.core.links;

import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.tracking.dataBase.core.CategorySet;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;

import java.util.List;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.tracking.dataBase.core.CategorySet.CategorySet_Order;

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();

        // ID
        // CategorySet ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_CategorySet, CategorySet.class));
        // SolidCategory ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_SolidCategory, SolidCategory.class));
        dataObjectSchema.get(CategoryToCategorySet_SolidCategory).setManualCanEdit(true);
        dataObjectSchema.<SolidCategory>get(CategoryToCategorySet_SolidCategory).addFilter(new Dependant_FieldFilter<SolidCategory, CategoryToCategorySet>(CategoryToCategorySet_CategorySet) {
            @Override
            public boolean isValid(SolidCategory newValue, SolidCategory pastValue, CategoryToCategorySet categoryToCategorySet) {
                return !categoryToCategorySet.getCategorySet().getUsedCategories().contains(newValue) || newValue.equals(categoryToCategorySet.getSolidCategory());
            }
        });
        // OrderImpl ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_OrderImpl, Integer.class));
        dataObjectSchema.get(CategoryToCategorySet_OrderImpl).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(CategoryToCategorySet_OrderImpl).setManualCanEdit(true);
        // Order ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(CategoryToCategorySet_Order, Integer.class));
        dataObjectSchema.get(CategoryToCategorySet_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((CategoryToCategorySet) container).getCategorySet().getOrder() * 1000 + ((CategoryToCategorySet) container).getOrderImpl()
                                , new LocalSource.LocalSource_Factory<>((CategoryToCategorySet_OrderImpl))
                                , new ExternalSource.ExternalSource_Factory<>((CategoryToCategorySet_CategorySet), CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(CategoryToCategorySet.class);
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