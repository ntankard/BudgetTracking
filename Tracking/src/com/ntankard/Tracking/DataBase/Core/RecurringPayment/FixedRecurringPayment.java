package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public class FixedRecurringPayment extends RecurringPayment {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getValue", "getStart", "getEnd", "getBank", "getCategory"})
    public FixedRecurringPayment(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, Category category) {
        super(id, name, value, start, end, bank, category);
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
        for (RecurringBankTransfer recurringBankTransfer : getChildren(RecurringBankTransfer.class)) {
            if (recurringBankTransfer.getPeriod().getOrder() < getStart().getOrder()) {
                recurringBankTransfer.remove();
            } else if (getEnd() != null) {
                if (getEnd().getOrder() < recurringBankTransfer.getPeriod().getOrder()) {
                    recurringBankTransfer.remove();
                }
            }
        }

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (period.getOrder() >= getStart().getOrder()) {
                if (getEnd() != null) {
                    if (getEnd().getOrder() < period.getOrder()) {
                        continue;
                    }
                }
                int size = new TwoParent_Children_Set<>(RecurringBankTransfer.class, period, this).get().size();
                if (size > 1) {
                    throw new RuntimeException("Duplicate payment");
                }
                if (size == 0) {
                    new RecurringBankTransfer(TrackingDatabase.get().getNextId(), period, getBank(), getValue(), null, getCategory(), null, this).add();
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName
    // 1110000------getStart
    // 1120000------getEnd
    // 1130000------getBank
    // 1140000------getCategory
    // 1150000------getValue
    // 1160000------getCurrency
    // 2000000--getParents
    // 3000000--getChildren

}
