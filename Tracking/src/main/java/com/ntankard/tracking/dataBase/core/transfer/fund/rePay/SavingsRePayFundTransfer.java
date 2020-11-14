package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.SavingsFundEvent;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransferList;
import com.ntankard.javaObjectDatabase.coreObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.coreObject.field.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;

@ParameterMap(shouldSave = false)
public class SavingsRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String SavingsRePayFundTransfer_NonSavingsSet = "getNonSavingsSet";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            SavingsRePayFundTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, SavingsFundEvent.class,
            Transfer_Source, (generator, secondaryGenerator) -> SavingsRePayFundTransfer.make(
            generator.getTrackingDatabase().getNextId(),
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // NonSavingsSet ===============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(SavingsRePayFundTransfer_NonSavingsSet, HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
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
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((Transfer_Period))));
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, SavingsRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , new ListSource.ListSource_Factory<>(
                        SavingsRePayFundTransfer_NonSavingsSet,
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

        return dataObjectSchema.finaliseContainer(SavingsRePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static SavingsRePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        TrackingDatabase trackingDatabase = period.getTrackingDatabase();
        TrackingDatabase_Schema trackingDatabase_schema = trackingDatabase.getSchema();
        return assembleDataObject(trackingDatabase, trackingDatabase_schema.getClassSchema(SavingsRePayFundTransfer.class), new SavingsRePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }
}
