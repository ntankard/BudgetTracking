package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.Transfer_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.Dispaly.Util.Comparators.Ordered_Comparator;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public class FixedPeriodFundEvent extends FundEvent {

    // My parents
    private ExistingPeriod start;

    // My values
    private Integer duration;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCategory", "getStart", "getDuration"})
    public FixedPeriodFundEvent(Integer id, String name, Category category, ExistingPeriod start, Integer duration) {
        super(id, name, category);
        if (start == null) throw new IllegalArgumentException("Start was null");
        if (duration == null) throw new IllegalArgumentException("Duration is null");
        if (duration < 1) throw new IllegalArgumentException("Duration is less than 1");
        this.start = start;
        this.duration = duration;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(getStart());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        List<Period> periods = new ArrayList<>();

        if (!isChargeThisPeriod(period)) {
            for (ManualFundTransfer useCategoryFundTransfer : new OneParent_Children_Set<>(ManualFundTransfer.class, this).get()) {
                if (!periods.contains(useCategoryFundTransfer.getPeriod())) {
                    periods.add(useCategoryFundTransfer.getPeriod());
                }
            }
            if (periods.size() == 0) {
                return false;
            }
            periods.sort(new Ordered_Comparator());

            if (period.getOrder() < getStart().getOrder()) {
                return period.getOrder() >= periods.get(0).getOrder();
            } else {
                return period.getOrder() <= periods.get(periods.size() - 1).getOrder();
            }
        }
        return true;
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
        if (!isChargeThisPeriod(period)) {
            return -0.0;
        }
        return new Transfer_SumSet(new OneParent_Children_Set<>(HalfTransfer.class, this, new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class)), this).getTotal() / duration;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName
    // 1101000--------getCategory

    @DisplayProperties(order = 1101100)
    public ExistingPeriod getStart() {
        return start;
    }

    @DisplayProperties(order = 1101200)
    public Integer getDuration() {
        return duration;
    }

    // 1110000------getOrder
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setter #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category is null");
        this.category.notifyChildUnLink(this);
        this.category = category;
        this.category.notifyChildLink(this);

        for (FundTransfer fundTransfer : TrackingDatabase.get().get(FundTransfer.class)) {
            fundTransfer.setDestination();
        }
        recreateRePay();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        if (start == null) throw new IllegalArgumentException("Start was null");
        this.start.notifyChildUnLink(this);
        this.start = start;
        this.start.notifyChildLink(this);
        recreateRePay();
        validateParents();
    }

    public void setDuration(Integer duration) {
        if (duration == null) throw new IllegalArgumentException("Duration is null");
        if (duration < 1) throw new IllegalArgumentException("Duration is less than 1");
        this.duration = duration;
        recreateRePay();
        validateParents();
    }
}
