package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class NoneFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        return FundEvent.getFields();
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static NoneFundEvent make(Integer id, String name, SolidCategory solidCategory) {
        return assembleDataObject(NoneFundEvent.getFields(), new NoneFundEvent()
                , "getId", id
                , "getName", name
                , "getCategory", solidCategory
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isChargeThisPeriod(Period period) {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        throw new UnsupportedOperationException("Not relevant for this type");
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
