package com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.PeriodBoundFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class PeriodBoundFundEvent_Transfer extends PeriodFundTransfer {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public PeriodBoundFundEvent_Transfer(Integer id, String description, Period sourceContainer, Category sourceCategory, Fund destinationContainer, PeriodBoundFundEvent destinationCategory, Currency currency) {
        super(id, description, -1.0, sourceContainer, sourceCategory, destinationContainer, destinationCategory, currency);
    }

    /**
     * {@inheritDoc
     */
    @MemberProperties(verbosityLevel = MemberProperties.DEBUG_DISPLAY)
    @DisplayProperties(order = 20)
    @Override
    public Class<?> getTypeClass() {
        return PeriodFundTransfer.class;
    }

    /**
     * {@inheritDoc
     */
    @DisplayProperties(order = 4, dataType = CURRENCY)
    @Override
    public Double getValue() {
        return ((PeriodBoundFundEvent) getDestinationCategory()).getPeriodPay();
    }
}
