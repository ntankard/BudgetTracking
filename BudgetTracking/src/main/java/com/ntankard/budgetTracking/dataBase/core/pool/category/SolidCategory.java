package com.ntankard.budgetTracking.dataBase.core.pool.category;

import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Category_Summary;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;

public class SolidCategory extends Category implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String SolidCategory_Default = "isDefault";
    public static final String SolidCategory_Savings = "isSavings";
    public static final String SolidCategory_Taxable = "isTaxable";
    public static final String SolidCategory_Set = "getSet";
    public static final String SolidCategory_SetName = "getSetName";
    public static final String SolidCategory_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Category.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(Category_Summary.Factory);

        // ID
        // Name
        // Default =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Default, Boolean.class));
        dataObjectSchema.get(SolidCategory_Default).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        dataObjectSchema.get(SolidCategory_Default).setDefaultFlag(true);
        // Savings =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Savings, Boolean.class));
        dataObjectSchema.get(SolidCategory_Savings).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        dataObjectSchema.get(SolidCategory_Savings).setSpecialFlag(true);
        // Taxable =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Taxable, Boolean.class));
        dataObjectSchema.get(SolidCategory_Taxable).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        dataObjectSchema.get(SolidCategory_Taxable).setSpecialFlag(true);
        // Set =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Set, Integer.class));
        dataObjectSchema.get(SolidCategory_Set).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // SetName =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_SetName, String.class));
        dataObjectSchema.get(SolidCategory_SetName).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Order, Integer.class));
        dataObjectSchema.get(SolidCategory_Order).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(SolidCategory.class);
    }

    /**
     * Constructor
     */
    public SolidCategory(Database database, Object... args) {
        super(database, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Boolean isDefault() {
        return get(SolidCategory_Default);
    }

    public Boolean isSavings() {
        return get(SolidCategory_Savings);
    }

    public Boolean isTaxable() {
        return get(SolidCategory_Taxable);
    }

    public Integer getSet() {
        return get(SolidCategory_Set);
    }

    public String getSetName() {
        return get(SolidCategory_SetName);
    }

    @Override
    public Integer getOrder() {
        return get(SolidCategory_Order);
    }
}
