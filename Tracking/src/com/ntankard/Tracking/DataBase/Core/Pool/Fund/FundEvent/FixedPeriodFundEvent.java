package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.UseCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FixedPeriodFundEvent extends FundEvent {

    // My parents
    private Period start;

    // My values
    private Integer duration;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCategory", "getStart", "getDuration"})
    public FixedPeriodFundEvent(Integer id, String name, Category category, Period start, Integer duration) {
        super(id, name, category);
        this.start = start;
        this.duration = duration;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(start);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        return isChargeThisPeriod(period);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isChargeThisPeriod(Period period) {
        return period.isWithin(start, duration);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        return -new TransferSet_Summary<>(new Children_Set<>(UseCategoryFundTransfer.class, this), getFund()).getTotal() / duration;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 4)
    public Period getStart() {
        return start;
    }

    @DisplayProperties(order = 5)
    public Integer getDuration() {
        return duration;
    }
}
