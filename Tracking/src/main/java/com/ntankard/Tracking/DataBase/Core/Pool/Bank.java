package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public class Bank extends Pool implements CurrencyBound, Ordered, HasDefault {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Bank_Currency = "getCurrency";
    public static final String Bank_Start = "getStart";
    public static final String Bank_Default = "isDefault";
    public static final String Bank_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Pool.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(StatementEnd.Factory);
        fieldContainer.addObjectFactory(Bank_Summary.Factory);

        // ID
        // Name
        // Currency ========================================================================================================
        fieldContainer.add(new DataField<>(Bank_Currency, Currency.class));
        // Start ========================================================================================================
        fieldContainer.add(new DataField<>(Bank_Start, Double.class));
        fieldContainer.get(Bank_Start).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Default ========================================================================================================
        fieldContainer.add(new DataField<>(Bank_Default, Boolean.class));
        fieldContainer.get(Bank_Default).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Order ========================================================================================================
        fieldContainer.add(new DataField<>(Bank_Order, Integer.class));
        fieldContainer.get(Bank_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Bank.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Currency getCurrency() {
        return get(Bank_Currency);
    }

    public Double getStart() {
        return get(Bank_Start);
    }

    @Override
    public Boolean isDefault() {
        return get(Bank_Default);
    }

    @Override
    public Integer getOrder() {
        return get(Bank_Order);
    }
}
