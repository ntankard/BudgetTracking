package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.IntegerRange_FieldFilter;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.FundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
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
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = FundEvent.getFields();
        toReturn.add(new DataObject_Field<>("getStart", ExistingPeriod.class));
        toReturn.add(new Field<>("getDuration", Integer.class).addFilter(new IntegerRange_FieldFilter(1, null)));
        return toReturn;
    }

    /**
     * Create a new FixedPeriodFundEvent object
     */
    public static FixedPeriodFundEvent make(Integer id, String name, SolidCategory solidCategory, ExistingPeriod start, Integer duration) {
        return assembleDataObject(FixedPeriodFundEvent.getFields(), new FixedPeriodFundEvent()
                , "getId", id
                , "getName", name
                , "getCategory", solidCategory
                , "getStart", start
                , "getDuration", duration
        );
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
        return get("getStart");
    }

    @DisplayProperties(order = 1101200)
    public Integer getDuration() {
        return get("getDuration");
    }

    // 1110000------getOrder
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setter #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(SolidCategory solidCategory) {
        set("getCategory", solidCategory);

        for (FundTransfer fundTransfer : TrackingDatabase.get().get(FundTransfer.class)) {
            fundTransfer.setDestination();
        }
        recreateRePay();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        set("getStart", start);
        recreateRePay();
        validateParents();
    }

    public void setDuration(Integer duration) {
        set("getDuration", duration);
        recreateRePay();
        validateParents();
    }
}
