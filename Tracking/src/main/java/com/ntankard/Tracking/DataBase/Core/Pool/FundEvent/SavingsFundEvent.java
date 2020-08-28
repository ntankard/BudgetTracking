package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.ClassicRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Single_OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.dynamicGUI.CoreObject.Factory.Dummy_Factory;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
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
        fieldContainer.addObjectFactory(new Dummy_Factory(RePayFundTransfer.class));

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
        recreateRePay();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        for (RePayFundTransfer toRemove : new OneParent_Children_Set<>(RePayFundTransfer.class, this).get()) {
            toRemove.remove();
        }

        super.remove_impl();
    }

    /**
     * Create the repay objects (remove old ones)
     */
    protected void recreateRePay() {
        for (ClassicRePayFundTransfer toRemove : new OneParent_Children_Set<>(ClassicRePayFundTransfer.class, this).get()) {
            toRemove.remove();
        }

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (this.isChargeThisPeriod(period)) {
                ClassicRePayFundTransfer.make(TrackingDatabase.get().getNextId(), period, this, TrackingDatabase.get().getDefault(Currency.class)).add();
            }
        }
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
        return new Single_OneParent_Children_Set<>(Period_Summary.class, period).getItem().getNonSaveCategoryDelta();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        return get(NamedDataObject_Name);
    }
}
