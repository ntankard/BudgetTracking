package com.ntankard.budgetTracking.dataBase.core.transfer.fund;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.ALWAYS_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;

public abstract class FundTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Transfer.getDataObjectSchema();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        dataObjectSchema.add(Transfer_Period, new DataField_Schema<>(Transfer_Source, FundEvent.class));
        // Value
        // Currency ====================================================================================================
        dataObjectSchema.get(Transfer_Currency).getProperty(Display_Properties.class).setVerbosityLevel(ALWAYS_DISPLAY);
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Currency));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Period));
        // Parents
        // Children

        return dataObjectSchema.endLayer(FundTransfer.class);
    }

    /**
     * Constructor
     */
    public FundTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            List<T> toReturn = new ArrayList<>();
            for (FundEvent fundEvent : super.sourceOptions(FundEvent.class, fieldName)) {
                toReturn.add((T) fundEvent);
            }
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }
}
