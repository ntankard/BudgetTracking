package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent.FundEvent_Category;

public class ManualFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        fieldContainer.<FundEvent>get(Transfer_Source).setDataCore(new ValueRead_DataCore<>(true));
        // Value =======================================================================================================
        fieldContainer.get(Transfer_Value).setDataCore(new ValueRead_DataCore<>(true));
        // Currency
        // Destination =================================================================================================
        // TODO this was failing when it was set on the RePay, this might be because they were being recreated or because there is a problem here, test
        fieldContainer.<Pool>get(Transfer_Destination).setDataCore(new Derived_DataCore<>(new Derived_DataCore.DirectExternalSource<>(fieldContainer.get(Transfer_Source), FundEvent_Category)));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.finaliseContainer(ManualFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static ManualFundTransfer make(Integer id, String description, Period period, FundEvent source, Double value, Currency currency) {
        return assembleDataObject(ManualFundTransfer.getFieldContainer(), new ManualFundTransfer()
                , DataObject_Id, id
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , Transfer_Currency, currency
        );
    }
}
