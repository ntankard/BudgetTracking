package com.ntankard.Tracking.DataBase.Core.Transfers.RecurringPayment.Fixed;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FixedRecurringTransfer extends BankCategoryTransfer {

    // My parents
    private FixedRecurringPayment parentPayment;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public FixedRecurringTransfer(Integer id, Period period, Bank source, Category destination, FixedRecurringPayment parentPayment) {
        super(id, "NOT USED", -1.0, period, source, destination);
        this.parentPayment = parentPayment;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(getParentPayment());
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 3)
    public String getDescription() {
        return parentPayment.getName();
    }

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getSourceValue() {
        return -parentPayment.getValue();
    }

    @Override
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getDestinationValue() {
        return parentPayment.getValue();
    }

    @DisplayProperties(order = 11)
    public FixedRecurringPayment getParentPayment() {
        return parentPayment;
    }
}
