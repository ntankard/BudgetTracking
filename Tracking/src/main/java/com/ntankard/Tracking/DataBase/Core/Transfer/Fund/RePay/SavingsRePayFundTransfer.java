package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

@ParameterMap(shouldSave = false)
public class SavingsRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RePayFundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source
        // Value
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.finaliseContainer(SavingsRePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static SavingsRePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        return assembleDataObject(SavingsRePayFundTransfer.getFieldContainer(), new SavingsRePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
                , Transfer_Destination, source.getCategory() // TODO THIS IS BAD, this is happening because this object is always remade when a value is changed in the fund event, add proper listeners and remove this
        );
    }
}
