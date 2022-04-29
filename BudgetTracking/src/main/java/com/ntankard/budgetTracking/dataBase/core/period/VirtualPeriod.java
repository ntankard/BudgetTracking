package com.ntankard.budgetTracking.dataBase.core.period;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;

public class VirtualPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String VirtualPeriod_Name = "getName";
    public static final String VirtualPeriod_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Period.getDataObjectSchema();

        // ID
        // Name ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualPeriod_Name, String.class));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(VirtualPeriod_Order, Integer.class));
        dataObjectSchema.get(VirtualPeriod_Order).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(VirtualPeriod.class);
    }

    /**
     * Constructor
     */
    public VirtualPeriod(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public VirtualPeriod(Database database, String name, Integer order) {
        super(database
                , VirtualPeriod_Name, name
                , VirtualPeriod_Order, order
        );
    }

    /**
     * @inheritDoc
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
