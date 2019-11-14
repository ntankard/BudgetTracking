package com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodBoundFundEvent_Transfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class PeriodBoundFundEvent extends FundEvent {

    // My parent
    private Period start;
    private Category source;

    // My values
    private Integer duration;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getFund", "getStart", "getDuration", "getSource"})
    public PeriodBoundFundEvent(Integer id, String name, Fund fund, Period start, Integer duration, Category source) {
        super(id, name, fund);
        this.start = start;
        this.duration = duration;
        this.source = source;
    }

    /**
     * {@inheritDoc
     */
    @MemberProperties(verbosityLevel = MemberProperties.DEBUG_DISPLAY)
    @DisplayProperties(order = 20)
    @Override
    public Class<?> getTypeClass() {
        return FundEvent.class;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>(super.getParents());
        toReturn.add(start);
        toReturn.add(source);
        return toReturn;
    }

    public Double getPeriodPay() {
        double totalCost = 0.0;
        for (PeriodFundTransfer periodFundTransfer : getChildren(PeriodFundTransfer.class)) {
            if (!(periodFundTransfer instanceof PeriodBoundFundEvent_Transfer)) {
                totalCost += periodFundTransfer.getValue();
            }
        }

        return -totalCost / duration;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getStart() {
        return start;
    }

    public Integer getDuration() {
        return duration;
    }

    public Category getSource() {
        return source;
    }
}
