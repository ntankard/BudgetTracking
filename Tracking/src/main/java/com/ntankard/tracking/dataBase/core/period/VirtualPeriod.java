package com.ntankard.tracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.INFO_DISPLAY;

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
    public static VirtualPeriod make(TrackingDatabase trackingDatabase, Integer id, String name, Integer order) {
        TrackingDatabase_Schema trackingDatabase_schema = trackingDatabase.getSchema();
        return assembleDataObject(trackingDatabase, trackingDatabase_schema.getClassSchema(VirtualPeriod.class), new VirtualPeriod()
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
