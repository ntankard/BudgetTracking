package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore.HalfTransferSetSum_DataCore;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore.SingleParentSet_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.SetFilter;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import java.util.List;

@ParameterMap(shouldSave = false)
public class SavingsRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String SavingsRePayFundTransfer_NonSavingsSet = "getNonSavingsSet";


    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RePayFundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source
        // NonSavingsSet ===============================================================================================
        fieldContainer.add(new DataField<>(SavingsRePayFundTransfer_NonSavingsSet, List.class));
        fieldContainer.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet).setDataCore(
                new SingleParentSet_DataCore<>(
                        HalfTransfer.class,
                        fieldContainer.get(Transfer_Period),
                        new SetFilter<HalfTransfer>(null) {
                            @Override
                            protected boolean shouldAdd_Impl(HalfTransfer dataObject) {
                                if (SolidCategory.class.isAssignableFrom(dataObject.getPool().getClass())) { // this is in place instead of a loop of al solid categories
                                    if (dataObject.getTransfer() instanceof RePayFundTransfer) {
                                        RePayFundTransfer rePayCategoryFundTransfer = (RePayFundTransfer) dataObject.getTransfer();
                                        return !(rePayCategoryFundTransfer.getSource() instanceof SavingsFundEvent);
                                    }
                                    return true;
                                }
                                return false;
                            }
                        }));
        // Value =======================================================================================================
        fieldContainer.<Double>get(Transfer_Value).setDataCore(
                new HalfTransferSetSum_DataCore(
                        fieldContainer.get(SavingsRePayFundTransfer_NonSavingsSet), value -> -value));
        // =============================================================================================================
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
        );
    }
}
