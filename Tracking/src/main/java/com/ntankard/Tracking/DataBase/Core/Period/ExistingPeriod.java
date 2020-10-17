package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.FixedPeriodRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.SavingsRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.TaxRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

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
        fieldContainer.addObjectFactory(StatementEnd.Factory);
        fieldContainer.addObjectFactory(Bank_Summary.Factory);
        fieldContainer.addObjectFactory(SavingsRePayFundTransfer.Factory);
        fieldContainer.addObjectFactory(TaxRePayFundTransfer.Factory);
        fieldContainer.addObjectFactory(FixedPeriodRePayFundTransfer.Factory);
        fieldContainer.addObjectFactory(RecurringBankTransfer.Factory);

        // ID
        // Month =======================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Month, Integer.class));
        // Year ========================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Year, Integer.class));
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(ExistingPeriod_Order, Integer.class));
        fieldContainer.get(ExistingPeriod_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.get(ExistingPeriod_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((ExistingPeriod) container).getYear() * 12 + ((ExistingPeriod) container).getMonth()
                                , new LocalSource.LocalSource_Factory<>(ExistingPeriod_Month)
                                , new LocalSource.LocalSource_Factory<>(ExistingPeriod_Year)));
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
