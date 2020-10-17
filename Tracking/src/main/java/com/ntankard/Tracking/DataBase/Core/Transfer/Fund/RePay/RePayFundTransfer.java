package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import static com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject.NamedDataObject_Name;
import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent.FundEvent_Category;

@ParameterMap(shouldSave = false)
public abstract class RePayFundTransfer extends FundTransfer {

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
        fieldContainer.get(Transfer_Description).setManualCanEdit(false);
        fieldContainer.get(Transfer_Description).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternalSource.DirectExternalSource_Factory<>(Transfer_Source, NamedDataObject_Name,
                                original -> "RP " + original)));
        // Period
        // Source
        // Value
        // Currency
        // Destination =================================================================================================
        fieldContainer.get(Transfer_Destination).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternalSource.DirectExternalSource_Factory<>(Transfer_Source, FundEvent_Category)));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.endLayer(RePayFundTransfer.class);
    }
}
