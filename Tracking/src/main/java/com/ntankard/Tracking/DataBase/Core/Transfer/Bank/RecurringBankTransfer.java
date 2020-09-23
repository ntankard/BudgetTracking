package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent.NamedDataObject_Name;

public class RecurringBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String RecurringBankTransfer_ParentPayment = "getParentPayment";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = BankTransfer.getFieldContainer();

        // ID
        // Description (Below)
        // Period
        // Source ======================================================================================================
        fieldContainer.get(Transfer_Source).setCanEdit(true);
        //==============================================================================================================
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // DestinationValue
        // DestinationCurrency
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // ParentPayment ===============================================================================================
        fieldContainer.add(new DataField<>(RecurringBankTransfer_ParentPayment, FixedRecurringPayment.class));
        //==============================================================================================================
        // Parents
        // Children

        // Description =================================================================================================
        fieldContainer.<String>get(Transfer_Description).setDataCore(
                new Derived_DataCore<>(
                        new DirectExternalSource<>(fieldContainer.get(RecurringBankTransfer_ParentPayment), NamedDataObject_Name)));
        //==============================================================================================================

        return fieldContainer.finaliseContainer(RecurringBankTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static RecurringBankTransfer make(Integer id,
                                             Period period, Bank source, Double value,
                                             Period destinationPeriod, Pool destination, Double destinationValue,
                                             FixedRecurringPayment parentPayment) {
        return assembleDataObject(RecurringBankTransfer.getFieldContainer(), new RecurringBankTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , BankTransfer_DestinationValue, destinationValue
                , RecurringBankTransfer_ParentPayment, parentPayment
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public FixedRecurringPayment getParentPayment() {
        return get(RecurringBankTransfer_ParentPayment);
    }
}
