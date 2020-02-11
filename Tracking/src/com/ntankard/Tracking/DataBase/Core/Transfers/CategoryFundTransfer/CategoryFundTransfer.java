package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class CategoryFundTransfer extends Transfer<Category, FundEvent> {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public CategoryFundTransfer(Integer id, String description, Double value, Period period, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, fundEvent.getCategory(), fundEvent, currency);
        if (currency == null) throw new IllegalArgumentException("Currency is null");
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod
    // 1200000----getDescription

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1300000)
    public Category getSource() {
        return super.getSource();
    }

    // 1400000----getSourceValue
    // 1500000----getSourceCurrency

    @Override
    @DisplayProperties(order = 1600000)
    public FundEvent getDestination() {
        return super.getDestination();
    }

    // 1700000----getDestinationValue
    // 1800000----getDestinationCurrency
    // 2000000--getParents
    // 3000000--getChildren
}
