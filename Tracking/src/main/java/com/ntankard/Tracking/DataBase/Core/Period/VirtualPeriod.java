package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public class VirtualPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String VirtualPeriod_Name = "getName";
    public static final String VirtualPeriod_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Period.getFieldContainer();

        // ID
        // Name ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualPeriod_Name, String.class));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualPeriod_Order, Integer.class));
        dataObjectSchema.get(VirtualPeriod_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(VirtualPeriod.class);
    }

    /**
     * Create a new VirtualPeriod object
     */
    public static VirtualPeriod make(Integer id, String name, Integer order) {
        return assembleDataObject(VirtualPeriod.getFieldContainer(), new VirtualPeriod()
                , DataObject_Id, id
                , VirtualPeriod_Name, name
                , VirtualPeriod_Order, order
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getName() {
        return get(VirtualPeriod_Name);
    }

    @Override
    public Integer getOrder() {
        return get(VirtualPeriod_Order);
    }
}
