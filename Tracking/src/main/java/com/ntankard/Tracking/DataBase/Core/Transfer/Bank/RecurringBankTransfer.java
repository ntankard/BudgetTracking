package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;

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
        // Description =================================================================================================
        fieldContainer.<String>get(Transfer_Description).setDataCore(new Method_DataCore<>(container -> ((RecurringBankTransfer) container).getParentPayment().getName()));
        // Period
        // Source ======================================================================================================
        fieldContainer.<Bank>get(Transfer_Source).setDataCore(new ValueRead_DataCore<>(true));
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
        fieldContainer.add(new Tracking_DataField<>(RecurringBankTransfer_ParentPayment, FixedRecurringPayment.class));
        // Parents
        // Children

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
