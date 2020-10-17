package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.Ordered_FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Currency;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

public abstract class RecurringPayment extends NamedDataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String RecurringPayment_Start = "getStart";
    public static final String RecurringPayment_End = "getEnd";
    public static final String RecurringPayment_Bank = "getBank";
    public static final String RecurringPayment_Category = "getCategory";
    public static final String RecurringPayment_Value = "getValue";
    public static final String RecurringPayment_Currency = "getCurrency";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Start =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Start, ExistingPeriod.class, true));
        dataObjectSchema.get(RecurringPayment_Start).setManualCanEdit(true);
        // End =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_End, ExistingPeriod.class, true));
        dataObjectSchema.get(RecurringPayment_End).setManualCanEdit(true);
        // Bank ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Bank, Bank.class));
        dataObjectSchema.get(RecurringPayment_Bank).setManualCanEdit(true);
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Category, SolidCategory.class));
        dataObjectSchema.get(RecurringPayment_Category).setManualCanEdit(true);
        // Value =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Value, Double.class));
        dataObjectSchema.get(RecurringPayment_Value).setManualCanEdit(true);
        dataObjectSchema.get(RecurringPayment_Value).getDisplayProperties().setDataType(CURRENCY);
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringPayment_Currency, Currency.class));
        dataObjectSchema.<Currency>get(RecurringPayment_Currency).setDataCore_factory(new Derived_DataCore.Derived_DataCore_Factory<>(new DirectExternalSource.DirectExternalSource_Factory<>((RecurringPayment_Bank), Bank_Currency)));
        dataObjectSchema.get(RecurringPayment_Currency).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        dataObjectSchema.<ExistingPeriod>get(RecurringPayment_End).addFilter(new Ordered_FieldFilter<>(RecurringPayment_Start, Ordered_FieldFilter.OrderSequence.ABOVE));
        dataObjectSchema.<ExistingPeriod>get(RecurringPayment_Start).addFilter(new Ordered_FieldFilter<>(RecurringPayment_End, Ordered_FieldFilter.OrderSequence.BELOW));

        return dataObjectSchema.endLayer(RecurringPayment.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getStart() {
        return get(RecurringPayment_Start);
    }

    public ExistingPeriod getEnd() {
        return get(RecurringPayment_End);
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Start")) {
            List<T> all = super.sourceOptions(type, fieldName);
            if (getEnd() != null) {
                all.removeIf(t -> {
                    ExistingPeriod existingPeriod = (ExistingPeriod) t;
                    return existingPeriod.getOrder() >= getEnd().getOrder();
                });
            }
            all.remove(getEnd());
            return all;
        } else if (fieldName.equals("End")) {
            List<T> all = super.sourceOptions(type, fieldName);
            all.remove(getStart());
            all.removeIf(t -> {
                ExistingPeriod existingPeriod = (ExistingPeriod) t;
                return existingPeriod.getOrder() <= getStart().getOrder();
            });
            all.add(null);
            return all;
        }
        return super.sourceOptions(type, fieldName);
    }
}
