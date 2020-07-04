package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // ID
        // Name ========================================================================================================
        fieldContainer.get(NamedDataObject_Name).setDataCore(new Method_DataCore<>(container -> "Savings"));
        // =============================================================================================================
        // Category
        // Parents
        // Children

        return fieldContainer.finaliseContainer(SavingsFundEvent.class);
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static SavingsFundEvent make(Integer id, SolidCategory solidCategory) {
        return assembleDataObject(SavingsFundEvent.getFieldContainer(), new SavingsFundEvent()
                , DataObject_Id, id
                , FundEvent_Category, solidCategory
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

    @Override
    public String getName() {
        return get(NamedDataObject_Name);
    }
}
