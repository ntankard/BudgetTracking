package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransferList;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.util.SetFilter;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Value;

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
        fieldContainer.add(new ListDataField<>(SavingsRePayFundTransfer_NonSavingsSet, HalfTransferList.class));
        fieldContainer.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet).setDataCore(
                new Children_ListDataCore<>(
                        HalfTransfer.class,
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
                        },
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Transfer_Period))));
        // Value =======================================================================================================
        fieldContainer.<Double>get(Transfer_Value).setDataCore(
                new Derived_DataCore<Double, SavingsRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<HalfTransfer>) fieldContainer.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet),
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
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
