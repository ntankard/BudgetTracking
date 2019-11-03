package com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

@ClassExtensionProperties(includeParent = true)
public class SavingsChargeTransfer extends FundChargeTransfer {

    /**
     * Constructor
     */
    public SavingsChargeTransfer(Period source) {
        super(TrackingDatabase.get().getNextId(FundChargeTransfer.class),
                "Savings",
                0.0,
                source,
                TrackingDatabase.get().getSpecialValue(Fund.class, Fund.SAVINGS),
                TrackingDatabase.get().getDefault(Currency.class)
        );
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
