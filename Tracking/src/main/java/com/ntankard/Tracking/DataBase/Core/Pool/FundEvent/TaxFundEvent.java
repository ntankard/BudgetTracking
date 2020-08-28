package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.TaxRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Single_OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
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
        fieldContainer.addObjectFactory(new DoubleParentFactory<TaxRePayFundTransfer, TaxFundEvent, ExistingPeriod>(
                TaxRePayFundTransfer.class,
                ExistingPeriod.class,
                (generator, secondaryGenerator) -> TaxRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        secondaryGenerator,
                        generator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));

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
        return -Currency.round(new Single_OneParent_Children_Set<>(Period_Summary.class, period).getItem().getTaxableIncome() * getPercentage());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }
}
