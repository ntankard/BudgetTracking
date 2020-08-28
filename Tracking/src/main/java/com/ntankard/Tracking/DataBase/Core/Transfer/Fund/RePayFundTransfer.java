package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

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

        return fieldContainer.endLayer(RePayFundTransfer.class);
    }
}
