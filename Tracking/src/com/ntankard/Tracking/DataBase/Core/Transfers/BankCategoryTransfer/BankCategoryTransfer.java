package com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class BankCategoryTransfer extends Transfer<Bank, Category> implements Ordered {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public BankCategoryTransfer(Integer id, String description, Double value, Period period, Bank source, Category destination) {
        super(id, description, value, period, source, destination, null);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod
    // 1200000----getDescription

    @Override
    @DisplayProperties(order = 1300000)
    public Bank getSource() {
        return super.getSource();
    }

    // 1400000----getSourceValue

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getSourceCurrency() {
        return getSource().getCurrency();
    }

    @Override
    @DisplayProperties(order = 1600000)
    public Category getDestination() {
        return super.getDestination();
    }

    // 1700000----getDestinationValue

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1800000)
    public Currency getDestinationCurrency() {
        return getSource().getCurrency();
    }

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1810000)
    public Integer getOrder() {
        return getSource().getOrder() * 100 + getDestination().getOrder();
    }

    // 2000000--getParents
    // 3000000--getChildren
}
