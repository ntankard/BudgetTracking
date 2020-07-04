package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public class VirtualPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String VirtualPeriod_Name = "getName";
    public static final String VirtualPeriod_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Period.getFieldContainer();

        // ID
        // Name ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(VirtualPeriod_Name, String.class));
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(VirtualPeriod_Order, Integer.class));
        fieldContainer.get(VirtualPeriod_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(VirtualPeriod.class);
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
