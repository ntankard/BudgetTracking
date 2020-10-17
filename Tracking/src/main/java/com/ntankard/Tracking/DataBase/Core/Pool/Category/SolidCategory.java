package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.SpecialValues;

import java.util.ArrayList;
import java.util.List;

public class SolidCategory extends Category implements HasDefault, SpecialValues, Ordered {

    public static Integer SAVINGS = 1;
    public static Integer TAXABLE = 2;

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Category.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(Category_Summary.Factory);

        // ID
        // Name
        // Default =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Default, Boolean.class));
        dataObjectSchema.get(SolidCategory_Default).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Savings =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Savings, Boolean.class));
        dataObjectSchema.get(SolidCategory_Savings).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Taxable =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Taxable, Boolean.class));
        dataObjectSchema.get(SolidCategory_Taxable).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Set =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Set, Integer.class));
        dataObjectSchema.get(SolidCategory_Set).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // SetName =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_SetName, String.class));
        dataObjectSchema.get(SolidCategory_SetName).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(SolidCategory_Order, Integer.class));
        dataObjectSchema.get(SolidCategory_Order).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(SolidCategory.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementations ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isValue(Integer key) {
        if (key.equals(SAVINGS)) {
            return isSavings();
        }
        if (key.equals(TAXABLE)) {
            return isTaxable();
        }
        throw new IllegalArgumentException("Unexpected key: " + key);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Integer> toChangeGetKeys() {
        List<Integer> keys = new ArrayList<>();
        keys.add(TAXABLE);
        keys.add(SAVINGS);
        return keys;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
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
