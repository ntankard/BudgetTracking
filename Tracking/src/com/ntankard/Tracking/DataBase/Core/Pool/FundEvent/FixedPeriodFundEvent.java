package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.IntegerRange_FieldFilter;
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

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public class FixedPeriodFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, Category category, ExistingPeriod start, Integer duration, DataObject container) {
        List<Field<?>> toReturn = FundEvent.getFields(id, name, category, container);
        toReturn.add(new DataObject_Field<>("start", ExistingPeriod.class, start, container));
        toReturn.add(new Field<>("duration", Integer.class, duration, container).addFilter(new IntegerRange_FieldFilter(1, null)));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCategory", "getStart", "getDuration"})
    public FixedPeriodFundEvent(Integer id, String name, Category category, ExistingPeriod start, Integer duration) {
        super();
        setFields(getFields(id, name, category, start, duration, this));
    }


    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

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
        return period.isWithin(getStart(), getDuration());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        if (!isChargeThisPeriod(period)) {
            return -0.0;
        }
        return new Transfer_SumSet<>(new OneParent_Children_Set<>(HalfTransfer.class, this, new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class)), this).getTotal() / getDuration();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName
    // 1101000--------getCategory

    @DisplayProperties(order = 1101100)
    public ExistingPeriod getStart() {
        return get("start");
    }

    @DisplayProperties(order = 1101200)
    public Integer getDuration() {
        return get("duration");
    }

    // 1110000------getOrder
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setter #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(Category category) {
        set("category", category);

        for (FundTransfer fundTransfer : TrackingDatabase.get().get(FundTransfer.class)) {
            fundTransfer.setDestination();
        }
        recreateRePay();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        set("start", start);
        recreateRePay();
        validateParents();
    }

    public void setDuration(Integer duration) {
        set("duration", duration);
        recreateRePay();
        validateParents();
    }
}
