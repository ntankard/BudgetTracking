package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, Category category, DataObject container) {
        return FundEvent.getFields(id, name, category, container);
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getCategory"})
    public SavingsFundEvent(Integer id, Category category) {
        super();
        setFields(getFields(id, "Saving", category, this));
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
        return new Period_Summary(period).getNonSaveCategoryDelta();
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
