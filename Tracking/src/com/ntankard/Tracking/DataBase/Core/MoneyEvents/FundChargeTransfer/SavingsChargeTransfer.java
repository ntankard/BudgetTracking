package com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

@ClassExtensionProperties(includeParent = true)
public class SavingsChargeTransfer extends FundChargeTransfer {

    /**
     * Constructor
     */
    public SavingsChargeTransfer(Period source) {
        super(TrackingDatabase.get().getNextId(FundChargeTransfer.class),
                source,
                TrackingDatabase.get().get(Fund.class, "Savings"),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                "Savings",
                0.0);
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @Override
    public Class<?> getTypeClass() {
        return FundChargeTransfer.class;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Double getValue() {
        double total = new PeriodTransaction_Summary(getSourceContainer()).getNonCategory();

        for (FundChargeTransfer fundChargeTransfer : getSourceContainer().getChildren(FundChargeTransfer.class)) {
            if (!(fundChargeTransfer instanceof SavingsChargeTransfer)) {
                total -= fundChargeTransfer.getValue();
            }
        }

        return total;
    }
}
