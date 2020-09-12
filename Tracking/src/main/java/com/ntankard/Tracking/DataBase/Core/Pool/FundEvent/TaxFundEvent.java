package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.TaxRePayFundTransfer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

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
        fieldContainer.add(new DataField<>(TaxFundEvent_Percentage, Double.class));
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
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }
}
