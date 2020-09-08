package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

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
        fieldContainer.get(Transfer_Description).setDataCore(
                new Derived_DataCore<>(
                        new Derived_DataCore.DirectExternalSource<>(fieldContainer.get(Transfer_Source), NamedDataObject_Name,
                                original -> "RP " + original)));
        // Period
        // Source
        // Value
        // Currency
        // Destination =================================================================================================
        fieldContainer.get(Transfer_Destination).setDataCore(
                new Derived_DataCore<>(
                        new Derived_DataCore.DirectExternalSource<>(fieldContainer.get(Transfer_Source), FundEvent_Category)));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.endLayer(RePayFundTransfer.class);
    }
}