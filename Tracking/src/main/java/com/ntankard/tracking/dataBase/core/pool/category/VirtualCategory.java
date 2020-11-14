package com.ntankard.tracking.dataBase.core.pool.category;

import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.coreObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.CategorySet;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.DEBUG_DISPLAY;
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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Category.getFieldContainer();

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
                                , new LocalSource.LocalSource_Factory<>(VirtualCategory_OrderImpl)
                                , new ExternalSource.ExternalSource_Factory<>(VirtualCategory_CategorySet, CategorySet_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(VirtualCategory.class);
    }

    /**
     * Create a new VirtualCategory object
     */
    public static VirtualCategory make(Integer id, String name, CategorySet categorySet, Integer orderImpl) {
        Database database = categorySet.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(VirtualCategory.class), new VirtualCategory()
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
