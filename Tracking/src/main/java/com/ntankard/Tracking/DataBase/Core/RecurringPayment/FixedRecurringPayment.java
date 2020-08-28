package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.SetFilter;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.dynamicGUI.CoreObject.Factory.Dummy_Factory;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public class FixedRecurringPayment extends RecurringPayment {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RecurringPayment.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(RePayFundTransfer.class));

        // ID
        // Name
        // Start
        // End
        // Bank
        // Category
        // Value
        // Currency
        // Parents
        // Children

        return fieldContainer.finaliseContainer(FixedRecurringPayment.class);
    }

    /**
     * Create a new FixedRecurringPayment object
     */
    public static FixedRecurringPayment make(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, SolidCategory solidCategory) {
        return assembleDataObject(FixedRecurringPayment.getFieldContainer(), new FixedRecurringPayment()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , RecurringPayment_Value, value
                , RecurringPayment_Start, start
                , RecurringPayment_End, end
                , RecurringPayment_Bank, bank
                , RecurringPayment_Category, solidCategory
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
    }

    /**
     * Create all the children transactions
     */
    public void regenerateChildren() {
        // TODO this is totally broken, because other objects like receits can link to this is means there are tons of duplicates left over, this all has to be reworked to set the values instead of remaking

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (period.getOrder() >= getStart().getOrder()) {
                if (getEnd() != null) {
                    if (getEnd().getOrder() < period.getOrder()) {
                        continue;
                    }
                }
                int size = new TwoParent_Children_Set<>(RecurringBankTransfer.class, period, this, new SetFilter<RecurringBankTransfer>(null) {
                    @Override
                    protected boolean shouldAdd_Impl(RecurringBankTransfer dataObject) {
                        return dataObject.toChangeGetDestinationTransfer().getPeriod().equals(period);
                    }
                }).get().size();
                if (size > 1) {
                    //throw new RuntimeException("Duplicate payment"); // @TODO need a better check for this, mby warning?
                }
                if (size == 0) {
                    RecurringBankTransfer.make(TrackingDatabase.get().getNextId(), period, getBank(), getValue(), null, getCategory(), null, this).add();
                }
            }
        }
    }
}
