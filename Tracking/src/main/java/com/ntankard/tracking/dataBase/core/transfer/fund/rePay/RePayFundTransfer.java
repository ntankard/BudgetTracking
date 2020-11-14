package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.tracking.dataBase.core.transfer.fund.FundTransfer;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import static com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject.NamedDataObject_Name;
import static com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent.FundEvent_Category;

@ParameterMap(shouldSave = false)
public abstract class RePayFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundTransfer.getFieldContainer();

        // ID
        // Description =================================================================================================
        dataObjectSchema.get(Transfer_Description).setManualCanEdit(false);
        dataObjectSchema.get(Transfer_Description).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternal_Source.DirectExternalSource_Factory<>(Transfer_Source, NamedDataObject_Name,
                                original -> "RP " + original)));
        // Period
        // Source
        // Value
        // Currency
        // Destination =================================================================================================
        dataObjectSchema.get(Transfer_Destination).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternal_Source.DirectExternalSource_Factory<>(Transfer_Source, FundEvent_Category)));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.endLayer(RePayFundTransfer.class);
    }
}
