package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.CategorySet;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.Tracking.DataBase.Core.CategorySet.CategorySet_Order;

public class VirtualCategory extends Category implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String VirtualCategory_CategorySet = "getCategorySet";
    public static final String VirtualCategory_OrderImpl = "getOrderImpl";
    public static final String VirtualCategory_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Category.getFieldContainer();

        // ID
        // Name
        // CategorySet =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(VirtualCategory_CategorySet, CategorySet.class));
        // OrderImpl ===================================================================================================
        fieldContainer.add(new Tracking_DataField<>(VirtualCategory_OrderImpl, Integer.class));
        fieldContainer.get(VirtualCategory_OrderImpl).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.get(VirtualCategory_OrderImpl).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(VirtualCategory_Order, Integer.class));
        fieldContainer.get(VirtualCategory_Order).setDataCore(
                new Derived_DataCore<>
                        (container -> ((VirtualCategory) container).getCategorySet().getOrder() * 1000 + ((VirtualCategory) container).getOrderImpl()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(VirtualCategory_OrderImpl))
                                , new Derived_DataCore.ExternalSource<>(fieldContainer.get(VirtualCategory_CategorySet), CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(VirtualCategory.class);
    }

    /**
     * Create a new VirtualCategory object
     */
    public static VirtualCategory make(Integer id, String name, CategorySet categorySet, Integer orderImpl) {
        return assembleDataObject(VirtualCategory.getFieldContainer(), new VirtualCategory()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , VirtualCategory_CategorySet, categorySet
                , VirtualCategory_OrderImpl, orderImpl
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

    public CategorySet getCategorySet() {
        return get(VirtualCategory_CategorySet);
    }

    public Integer getOrderImpl() {
        return get(VirtualCategory_OrderImpl);
    }

    @Override
    public Integer getOrder() {
        return get(VirtualCategory_Order);
    }
}
