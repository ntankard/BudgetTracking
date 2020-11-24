package com.ntankard.tracking.dataBase.core.recurringPayment;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import static com.ntankard.tracking.dataBase.core.pool.Bank.Bank_Currency;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;

public abstract class RecurringPayment extends NamedDataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String RecurringPayment_Start = "getStart";
    public static final String RecurringPayment_Bank = "getBank";
    public static final String RecurringPayment_Category = "getCategory";
    public static final String RecurringPayment_Value = "getValue";
    public static final String RecurringPayment_Currency = "getCurrency";
    public static final String RecurringPayment_Duration = "getDuration";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Start =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Start, ExistingPeriod.class));
        dataObjectSchema.get(RecurringPayment_Start).setManualCanEdit(true);
        // Bank ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Bank, Bank.class));
        dataObjectSchema.get(RecurringPayment_Bank).setManualCanEdit(true);
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Category, SolidCategory.class));
        dataObjectSchema.get(RecurringPayment_Category).setManualCanEdit(true);
        // Value =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Value, Double.class));
        dataObjectSchema.get(RecurringPayment_Value).setManualCanEdit(true);
        dataObjectSchema.get(RecurringPayment_Value).getProperty(Display_Properties.class).setDataType(CURRENCY);
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Currency, Currency.class));
        dataObjectSchema.<Currency>get(RecurringPayment_Currency).setDataCore_factory(new Derived_DataCore.Derived_DataCore_Factory<>(new DirectExternal_Source.DirectExternalSource_Factory<>((RecurringPayment_Bank), Bank_Currency)));
        dataObjectSchema.get(RecurringPayment_Currency).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        // Duration =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Duration, Integer.class, true));
        dataObjectSchema.get(RecurringPayment_Duration).setManualCanEdit(true);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(RecurringPayment.class);
    }

    /**
     * Constructor
     */
    public RecurringPayment(Database database) {
        super(database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getStart() {
        return get(RecurringPayment_Start);
    }

    public Bank getBank() {
        return get(RecurringPayment_Bank);
    }

    public SolidCategory getCategory() {
        return get(RecurringPayment_Category);
    }

    public Double getValue() {
        return get(RecurringPayment_Value);
    }

    public Currency getCurrency() {
        return get(RecurringPayment_Currency);
    }

    public Integer getDuration(){ return get(RecurringPayment_Duration);}
}
