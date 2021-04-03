package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.TaxFundEvent;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.tracking.dataBase.core.pool.fundEvent.TaxFundEvent.TaxFundEvent_Percentage;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;

@ParameterMap(shouldSave = false)
public class TaxRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxRePayFundTransfer_TaxableCategory = "getTaxableCategory";
    public static final String TaxRePayFundTransfer_TaxableSet = "getTaxableSet";
    public static final String TaxRePayFundTransfer_TaxableAmount = "getTaxableAmount";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            TaxRePayFundTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, TaxFundEvent.class,
            Transfer_Source, (generator, secondaryGenerator) -> new TaxRePayFundTransfer(
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
        // TaxableCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableCategory, Category.class));
        dataObjectSchema.<SolidCategory>get(TaxRePayFundTransfer_TaxableCategory).setDataCore_schema(
                new Static_DataCore_Schema<>(dataField ->
                        dataField.getContainer().getTrackingDatabase().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE)));
        // TaxableSet ==================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(TaxRePayFundTransfer_TaxableSet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet).setDataCore_schema(
                createMultiParentList(
                        HalfTransfer.class,
                        TaxRePayFundTransfer_TaxableCategory,
                        Transfer_Period));
        // TaxableAmount ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableAmount, Double.class));
        dataObjectSchema.<Double>get(TaxRePayFundTransfer_TaxableAmount).setDataCore_schema(
                new Derived_DataCore_Schema<Double, TaxRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        TaxRePayFundTransfer_TaxableSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, TaxRePayFundTransfer>) container -> {
                            TaxFundEvent taxFundEvent = (TaxFundEvent) container.getSource();
                            if (!container.getPeriod().isWithin(taxFundEvent.getStart(), taxFundEvent.getDuration())) {
                                return -0.0;
                            }
                            return Currency.round(container.getTaxableAmount() * taxFundEvent.getPercentage());
                        }
                        , new End_Source_Schema<>(TaxRePayFundTransfer_TaxableAmount)
                        , Source_Factory.makeSourceChain(Transfer_Source, TaxFundEvent_Percentage)));
        //==============================================================================================================
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(TaxRePayFundTransfer.class);
    }

    /**
     * Constructor
     */
    public TaxRePayFundTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public TaxRePayFundTransfer(Period period, FundEvent source, Currency currency) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }

    public Double getTaxableAmount() {
        return this.<Double>get(TaxRePayFundTransfer_TaxableAmount);
    }
}
