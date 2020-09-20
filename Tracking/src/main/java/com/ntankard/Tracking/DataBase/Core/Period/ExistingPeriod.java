package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.TaxFundEvent;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.FixedPeriodRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.SavingsRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.TaxRePayFundTransfer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.Dummy_Factory;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public class ExistingPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String ExistingPeriod_Month = "getMonth";
    public static final String ExistingPeriod_Year = "getYear";
    public static final String ExistingPeriod_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Period.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(RecurringBankTransfer.class));
        fieldContainer.addObjectFactory(new DoubleParentFactory<StatementEnd, ExistingPeriod, Bank>(
                StatementEnd.class,
                Bank.class,
                (generator, secondaryGenerator) -> StatementEnd.make(
                        TrackingDatabase.get().getNextId(),
                        generator,
                        secondaryGenerator,
                        0.0),
                ObjectFactory.GeneratorMode.SINGLE));
        fieldContainer.addObjectFactory(new DoubleParentFactory<Bank_Summary, ExistingPeriod, Bank>(
                Bank_Summary.class,
                Bank.class,
                (generator, secondaryGenerator) -> Bank_Summary.make(
                        TrackingDatabase.get().getNextId(),
                        generator,
                        secondaryGenerator)));
        fieldContainer.addObjectFactory(new DoubleParentFactory<SavingsRePayFundTransfer, ExistingPeriod, SavingsFundEvent>(
                SavingsRePayFundTransfer.class,
                SavingsFundEvent.class,
                (generator, secondaryGenerator) -> SavingsRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        generator,
                        secondaryGenerator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));
        fieldContainer.addObjectFactory(new DoubleParentFactory<TaxRePayFundTransfer, ExistingPeriod, TaxFundEvent>(
                TaxRePayFundTransfer.class,
                TaxFundEvent.class,
                (generator, secondaryGenerator) -> TaxRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        generator,
                        secondaryGenerator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));
        fieldContainer.addObjectFactory(new DoubleParentFactory<FixedPeriodRePayFundTransfer, ExistingPeriod, FixedPeriodFundEvent>(
                FixedPeriodRePayFundTransfer.class,
                FixedPeriodFundEvent.class,
                (generator, secondaryGenerator) -> FixedPeriodRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        generator,
                        secondaryGenerator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));

        // ID
        // Month =======================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Month, Integer.class));
        // Year ========================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Year, Integer.class));
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Order, Integer.class));
        fieldContainer.get(ExistingPeriod_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.get(ExistingPeriod_Order).setDataCore(
                new Derived_DataCore<>
                        (container -> ((ExistingPeriod) container).getYear() * 12 + ((ExistingPeriod) container).getMonth()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(ExistingPeriod_Month))
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(ExistingPeriod_Year))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(ExistingPeriod.class);
    }

    /**
     * Create a new ExistingPeriod object
     */
    public static ExistingPeriod make(Integer id, Integer month, Integer year) {
        return assembleDataObject(ExistingPeriod.getFieldContainer(), new ExistingPeriod()
                , DataObject_Id, id
                , ExistingPeriod_Month, month
                , ExistingPeriod_Year, year
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        for (FixedRecurringPayment fixedRecurringPayment : TrackingDatabase.get().get(FixedRecurringPayment.class)) {
            fixedRecurringPayment.regenerateChildren();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public ExistingPeriod generateNext() {
        int nextMonth = getMonth();
        int nextYear = getYear();
        nextMonth++;
        if (nextMonth > 12) {
            nextMonth -= 12;
            nextYear++;
        }

        return ExistingPeriod.make(TrackingDatabase.get().getNextId(), nextMonth, nextYear);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getYear() + "-" + getMonth();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Integer getMonth() {
        return get(ExistingPeriod_Month);
    }

    public Integer getYear() {
        return get(ExistingPeriod_Year);
    }

    @Override
    public Integer getOrder() {
        return get(ExistingPeriod_Order);
    }
}
