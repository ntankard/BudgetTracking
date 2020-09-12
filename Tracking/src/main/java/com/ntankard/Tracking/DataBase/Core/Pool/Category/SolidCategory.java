package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.SpecialValues;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Category.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new DoubleParentFactory<Category_Summary, SolidCategory, Period>(
                Category_Summary.class,
                Period.class,
                (generator, secondaryGenerator) -> Category_Summary.make(TrackingDatabase.get().getNextId(), secondaryGenerator, generator)));

        // ID
        // Name
        // Default =====================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_Default, Boolean.class));
        fieldContainer.get(SolidCategory_Default).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Savings =====================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_Savings, Boolean.class));
        fieldContainer.get(SolidCategory_Savings).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Taxable =====================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_Taxable, Boolean.class));
        fieldContainer.get(SolidCategory_Taxable).getDisplayProperties().setVerbosityLevel(Display_Properties.DEBUG_DISPLAY);
        // Set =========================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_Set, Integer.class));
        fieldContainer.get(SolidCategory_Set).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // SetName =====================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_SetName, String.class));
        fieldContainer.get(SolidCategory_SetName).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(SolidCategory_Order, Integer.class));
        fieldContainer.get(SolidCategory_Order).getDisplayProperties().setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(SolidCategory.class);
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
