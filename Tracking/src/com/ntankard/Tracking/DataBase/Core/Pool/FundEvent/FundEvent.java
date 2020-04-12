package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public abstract class FundEvent extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = Pool.getFields();
        toReturn.add(new DataObject_Field<>("getCategory", SolidCategory.class));
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        recreateRePay();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        for (RePayFundTransfer toRemove : new OneParent_Children_Set<>(RePayFundTransfer.class, this).get()) {
            toRemove.remove();
        }

        super.remove_impl();
    }

    /**
     * Create the repay objects (remove old ones)
     */
    protected void recreateRePay() {
        for (RePayFundTransfer toRemove : new OneParent_Children_Set<>(RePayFundTransfer.class, this).get()) {
            toRemove.remove();
        }

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (this.isChargeThisPeriod(period)) {
                RePayFundTransfer.make(TrackingDatabase.get().getNextId(), period, this, TrackingDatabase.get().getDefault(Currency.class)).add();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Is this fund event active in this period?
     *
     * @param period The period to check
     * @return True if its active at this time
     */
    public abstract Boolean isActiveThisPeriod(Period period);

    /**
     * Will there be a charge this period?
     *
     * @param period The period to test
     * @return True if there needs to be a charge for this period
     */
    public abstract Boolean isChargeThisPeriod(Period period);

    /**
     * Get the required charge amount for a specific period
     *
     * @param period The period to get
     * @return The required charge
     */
    public abstract Double getCharge(Period period);

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @DisplayProperties(order = 1101000)
    public SolidCategory getCategory() {
        return get("getCategory");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
