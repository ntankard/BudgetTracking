package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.dynamicGUI.CoreObject.Factory.Dummy_Factory;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public class TaxFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxFundEvent_Percentage = "getPercentage";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(RePayFundTransfer.class));

        // ID
        // Name
        // Category
        // Percentage ==================================================================================================
        fieldContainer.add(new Tracking_DataField<>(TaxFundEvent_Percentage, Double.class));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(TaxFundEvent.class);
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
        return -Currency.round(period.getPeriodSummary().getTaxableIncome() * getPercentage());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }
}
