package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.SavingsRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Static_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new DoubleParentFactory<SavingsRePayFundTransfer, SavingsFundEvent, ExistingPeriod>(
                SavingsRePayFundTransfer.class,
                ExistingPeriod.class,
                (generator, secondaryGenerator) -> SavingsRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        secondaryGenerator,
                        generator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));

        // ID
        // Name ========================================================================================================
        fieldContainer.get(NamedDataObject_Name).setDataCore(new Static_DataCore<>("Savings"));
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
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        return get(NamedDataObject_Name);
    }
}
