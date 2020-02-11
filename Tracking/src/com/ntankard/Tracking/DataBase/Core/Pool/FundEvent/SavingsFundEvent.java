package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayCategoryFundTransfer.class})
public class SavingsFundEvent extends FundEvent {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getCategory"})
    public SavingsFundEvent(Integer id, Category category) {
        super(id, "Savings", category, 2);
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
        return period instanceof ExistingPeriod;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        return -new Period_Summary(period).getNonSaveCategoryDelta();
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
    // 1110000------getOrder
    // 2000000--getParents
    // 3000000--getChildren
}
