package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
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
public class TaxFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, Category category, Double percentage, DataObject container) {
        List<Field<?>> toReturn = FundEvent.getFields(id, name, category, container);
        toReturn.add(new Field<>("percentage", Double.class, percentage, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCategory", "getPercentage"})
    public TaxFundEvent(Integer id, String name, Category category, Double percentage) {
        super();
        setFields(getFields(id, name, category, percentage, this));
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
        return -Currency.round(new Period_Summary(period).getTaxableIncome() * getPercentage());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName
    // 1101000--------getCategory

    @DisplayProperties(order = 1101100)
    public Double getPercentage() {
        return get("percentage");
    }

    // 1110000------getOrder
    // 2000000--getParents
    // 3000000--getChildren
}
