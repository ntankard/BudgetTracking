package com.ntankard.tracking.dataBase.core.period;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.StatementEnd;
import com.ntankard.tracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.FixedPeriodRePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.SavingsRePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.TaxRePayFundTransfer;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.INFO_DISPLAY;

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
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Period.getDataObjectSchema();

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
                                , new Local_Source.LocalSource_Factory<>(ExistingPeriod_Month)
                                , new Local_Source.LocalSource_Factory<>(ExistingPeriod_Year)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ExistingPeriod.class);
    }

    /**
     * Constructor
     */
    public ExistingPeriod(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public ExistingPeriod(Database database, Integer id, Integer month, Integer year) {
        this(database);
        setAllValues(DataObject_Id, id
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

        return new ExistingPeriod(getTrackingDatabase(), getTrackingDatabase().getNextId(), nextMonth, nextYear);
    }

    /**
     * @inheritDoc
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
