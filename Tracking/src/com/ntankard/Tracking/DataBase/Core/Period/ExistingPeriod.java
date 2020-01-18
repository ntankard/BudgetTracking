package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class, RePayCategoryFundTransfer.class})
public class ExistingPeriod extends Period {

    // My values
    private Integer month;
    private Integer year;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear"})
    public ExistingPeriod(Integer id, Integer month, Integer year) {
        super(id);
        this.month = month;
        this.year = year;
    }

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public ExistingPeriod generateNext() {
        int nextMonth = month;
        int nextYear = year;
        nextMonth++;
        if (nextMonth > 12) {
            nextMonth -= 12;
            nextYear++;
        }

        return new ExistingPeriod(TrackingDatabase.get().getNextId(), nextMonth, nextYear);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return year + "-" + month;
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Integer getMonth() {
        return month;
    }

    @DisplayProperties(order = 3)
    public Integer getYear() {
        return year;
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 4)
    public Integer getOrder() {
        return getYear() * 12 + getMonth();
    }
}
