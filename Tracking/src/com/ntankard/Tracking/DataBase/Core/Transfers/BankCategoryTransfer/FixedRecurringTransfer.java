package com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FixedRecurringTransfer extends BankCategoryTransfer {

    // My parents
    private FixedRecurringPayment parentPayment;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDestinationValue", "getPeriod", "getSource", "getDestination", "getParentPayment"})
    public FixedRecurringTransfer(Integer id, Double value, Period period, Bank source, Category destination, FixedRecurringPayment parentPayment) {
        super(id, "NOT USED", value, period, source, destination);
        if (parentPayment == null) throw new IllegalArgumentException("parentPayment is null");
        this.parentPayment = parentPayment;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(getParentPayment());
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod

    @Override
    @DisplayProperties(order = 1200000)
    public String getDescription() {
        return parentPayment.getName();
    }

    // 1300000----getSource
    // 1400000----getSourceValue
    // 1500000----getSourceCurrency
    // 1600000----getDestination
    // 1700000----getDestinationValue
    // 1800000----getDestinationCurrency

    @DisplayProperties(order = 1801000)
    public FixedRecurringPayment getParentPayment() {
        return parentPayment;
    }

    // 1810000------getOrder
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
