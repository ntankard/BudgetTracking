package com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.FundTransfer;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

import static com.ntankard.budgetTracking.dataBase.core.baseObject.NamedDataObject.NamedDataObject_Name;
import static com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent.FundEvent_Category;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

@ParameterMap(shouldSave = false)
public abstract class RePayFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundTransfer.getDataObjectSchema();

        // ID
        // Description =================================================================================================
        dataObjectSchema.get(Transfer_Description).setManualCanEdit(false);
        dataObjectSchema.<String>get(Transfer_Description).setDataCore_schema(
                new Derived_DataCore_Schema<String, RePayFundTransfer>
                        (dataObject -> "RP " + dataObject.getSource().getName()
                                , makeSourceChain(Transfer_Source, NamedDataObject_Name)));
        // Period
        // Source
        // Value
        // Currency
        // Destination =================================================================================================
        dataObjectSchema.<Pool>get(Transfer_Destination).setDataCore_schema(createDirectDerivedDataCore(Transfer_Source, FundEvent_Category));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.endLayer(RePayFundTransfer.class);
    }

    /**
     * Constructor
     */
    public RePayFundTransfer(Database database, Object... args) {
        super(database, args);
    }
}
