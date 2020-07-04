package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ParameterMap(shouldSave = false)
public class RePayFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundTransfer.getFieldContainer();

        // ID
        // Description =================================================================================================
        fieldContainer.get(Transfer_Description).setDataCore(new Method_DataCore<>(container -> "RP " + ((RePayFundTransfer) container).getSource().getName()));
        // Period
        // Source
        // Value =======================================================================================================
        // TODO this is a terrible hack. The change listener should not be needed because this object is fully regenerated ever time, but this is terible too
        while (fieldContainer.get(Transfer_Value).getFieldChangeListeners().size() != 0) {
            fieldContainer.get(Transfer_Value).removeChangeListener(fieldContainer.get(Transfer_Value).getFieldChangeListeners().get(0));
        }
        fieldContainer.get(Transfer_Value).setDataCore(new Method_DataCore<>(container -> ((FundEvent) ((RePayFundTransfer) container).getSource()).getCharge(((RePayFundTransfer) container).getPeriod())));
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children


        return fieldContainer.finaliseContainer(RePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static RePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        return assembleDataObject(RePayFundTransfer.getFieldContainer(), new RePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
                , Transfer_Destination, source.getCategory() // TODO THIS IS BAD, this is happening because this object is always remade when a value is changed in the fund event, add proper listeners and remove this
        );
    }
}
