package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
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
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = FundEvent.getFields();
        toReturn.remove(makeFieldMap(toReturn).get("getName"));
        return toReturn;
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static SavingsFundEvent make(Integer id, Category category) {
        return assembleDataObject(SavingsFundEvent.getFields(), new SavingsFundEvent()
                , "getId", id
                , "getCategory", category
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
        return Period_Summary.make(period).getNonSaveCategoryDelta();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @Override
    @DisplayProperties(order = 1100000)
    public String getName() {
        return "Savings";
    }

    // 1101000--------getCategory
    // 1110000------getOrder
    // 2000000--getParents
    // 3000000--getChildren
}
