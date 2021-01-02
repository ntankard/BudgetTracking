package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.SavingsFundEvent;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransferList;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
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
            Transfer_Source, (generator, secondaryGenerator) -> new SavingsRePayFundTransfer(
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // NonSavingsSet ===============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(SavingsRePayFundTransfer_NonSavingsSet, HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet).setDataCore_schema(
                createMultiParentList(
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
                        }, Transfer_Period
                ));
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<Double, SavingsRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(SavingsRePayFundTransfer_NonSavingsSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
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
     * Constructor
     */
    public SavingsRePayFundTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public SavingsRePayFundTransfer(Period period, FundEvent source, Currency currency) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }
}
