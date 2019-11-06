package com.ntankard.Tracking.DataBase.Core.SupportObjects;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.ManagersChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.TaxChargeTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

public class FundController extends DataObject {

    // My parents
    private Period period;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getPeriod"})
    public FundController(String id, Period period) {
        super(id);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(period);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getPeriod() {
        return period;
    }

    public Double getValue(ManagersChargeTransfer managersChargeTransfer) {
        double total = new PeriodTransaction_Summary(period).getNonCategory();

        int toSplit = 0;
        for (FundChargeTransfer fundChargeTransfer : period.getChildren(FundChargeTransfer.class)) {
            if (fundChargeTransfer instanceof ManagersChargeTransfer) {
                toSplit++;
            } else if (fundChargeTransfer instanceof TaxChargeTransfer) {
                total -= fundChargeTransfer.getValue();
            }
        }
        if (total < 0.0) {
            return 0.0;
        }

        return (total / 2) / toSplit;
    }
}
