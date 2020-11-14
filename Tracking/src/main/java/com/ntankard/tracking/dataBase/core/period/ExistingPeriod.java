package com.ntankard.tracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.StatementEnd;
import com.ntankard.tracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.FixedPeriodRePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.SavingsRePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.TaxRePayFundTransfer;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.INFO_DISPLAY;

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Period.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(StatementEnd.Factory);
        dataObjectSchema.addObjectFactory(Bank_Summary.Factory);
        dataObjectSchema.addObjectFactory(SavingsRePayFundTransfer.Factory);
        dataObjectSchema.addObjectFactory(TaxRePayFundTransfer.Factory);
        dataObjectSchema.addObjectFactory(FixedPeriodRePayFundTransfer.Factory);
        dataObjectSchema.addObjectFactory(RecurringBankTransfer.Factory);

        // ID
        // Month =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(ExistingPeriod_Month, Integer.class));
        // Year ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(ExistingPeriod_Year, Integer.class));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(ExistingPeriod_Order, Integer.class));
        dataObjectSchema.get(ExistingPeriod_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.get(ExistingPeriod_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((ExistingPeriod) container).getYear() * 12 + ((ExistingPeriod) container).getMonth()
                                , new LocalSource.LocalSource_Factory<>(ExistingPeriod_Month)
                                , new LocalSource.LocalSource_Factory<>(ExistingPeriod_Year)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ExistingPeriod.class);
    }

    /**
     * Create a new ExistingPeriod object
     */
    public static ExistingPeriod make(Database database, Integer id, Integer month, Integer year) {
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(ExistingPeriod.class), new ExistingPeriod()
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

        return ExistingPeriod.make(getTrackingDatabase(), getTrackingDatabase().getNextId(), nextMonth, nextYear);
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
