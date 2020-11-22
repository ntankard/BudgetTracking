package com.ntankard.tracking.dataBase.core.pool.category;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.CategorySet;

import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.tracking.dataBase.core.CategorySet.CategorySet_Order;

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
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Category.getDataObjectSchema();

        // ID
        // Name
        // CategorySet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualCategory_CategorySet, CategorySet.class));
        // OrderImpl ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualCategory_OrderImpl, Integer.class));
        dataObjectSchema.get(VirtualCategory_OrderImpl).setManualCanEdit(true);
        dataObjectSchema.get(VirtualCategory_OrderImpl).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualCategory_Order, Integer.class));
        dataObjectSchema.get(VirtualCategory_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((VirtualCategory) container).getCategorySet().getOrder() * 1000 + ((VirtualCategory) container).getOrderImpl()
                                , new Local_Source.LocalSource_Factory<>(VirtualCategory_OrderImpl)
                                , new External_Source.ExternalSource_Factory<>(VirtualCategory_CategorySet, CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(VirtualCategory.class);
    }

    /**
     * Constructor
     */
    public VirtualCategory(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public VirtualCategory(String name, CategorySet categorySet, Integer orderImpl) {
        this(categorySet.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , NamedDataObject_Name, name
                , VirtualCategory_CategorySet, categorySet
                , VirtualCategory_OrderImpl, orderImpl
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
