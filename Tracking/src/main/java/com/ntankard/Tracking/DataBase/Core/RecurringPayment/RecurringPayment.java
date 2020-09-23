package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.Ordered_FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.RePayFundTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.Dummy_Factory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = NamedDataObject.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(RePayFundTransfer.class));

        // ID
        // Name
        // Start =======================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_Start, ExistingPeriod.class, true));
        fieldContainer.get(RecurringPayment_Start).setCanEdit(true);
        fieldContainer.get(RecurringPayment_Start).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((RecurringPayment) field.getContainer()).regenerateChildren();
            }
        });
        // End =========================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_End, ExistingPeriod.class, true));
        fieldContainer.get(RecurringPayment_End).setCanEdit(true);
        fieldContainer.get(RecurringPayment_End).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((RecurringPayment) field.getContainer()).regenerateChildren();
            }
        });
        // Bank ========================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_Bank, Bank.class));
        fieldContainer.get(RecurringPayment_Bank).setCanEdit(true);
        fieldContainer.get(RecurringPayment_Bank).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((RecurringPayment) field.getContainer()).regenerateChildren();
            }
        });
        // Category ====================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_Category, SolidCategory.class));
        fieldContainer.get(RecurringPayment_Category).setCanEdit(true);
        fieldContainer.get(RecurringPayment_Category).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((RecurringPayment) field.getContainer()).regenerateChildren();
            }
        });
        // Value =======================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_Value, Double.class));
        fieldContainer.get(RecurringPayment_Value).setCanEdit(true);
        fieldContainer.get(RecurringPayment_Value).getDisplayProperties().setDataType(CURRENCY);
        // Currency ====================================================================================================
        fieldContainer.add(new DataField<>(RecurringPayment_Currency, Currency.class));
        fieldContainer.<Currency>get(RecurringPayment_Currency).setDataCore(new Derived_DataCore<>(new DirectExternalSource<>(fieldContainer.get(RecurringPayment_Bank), Bank_Currency)));
        fieldContainer.get(RecurringPayment_Currency).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        fieldContainer.<ExistingPeriod>get(RecurringPayment_End).addFilter(new Ordered_FieldFilter<>(RecurringPayment_Start, Ordered_FieldFilter.OrderSequence.ABOVE));
        fieldContainer.<ExistingPeriod>get(RecurringPayment_Start).addFilter(new Ordered_FieldFilter<>(RecurringPayment_End, Ordered_FieldFilter.OrderSequence.BELOW));

        return fieldContainer.endLayer(RecurringPayment.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        regenerateChildren();
    }

    /**
     * Create all the children transactions
     */
    public abstract void regenerateChildren();

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
