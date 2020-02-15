package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.FixedRecurringTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

@ClassExtensionProperties(includeParent = true)
public class FixedRecurringPayment extends RecurringPayment {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getValue", "getStart", "getEnd", "getBank", "getCategory"})
    public FixedRecurringPayment(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, Category category) {
        super(id, name, value, start, end, bank, category);
    }

    /**
     * Create all the children transactions
     */
    public void regenerateChildren() {
        for (FixedRecurringTransfer fixedRecurringTransfer : getChildren(FixedRecurringTransfer.class)) {
            if (fixedRecurringTransfer.getPeriod().getOrder() < getStart().getOrder()) {
                fixedRecurringTransfer.remove();
            }else if (getEnd() != null) {
                if (getEnd().getOrder() < fixedRecurringTransfer.getPeriod().getOrder()) {
                    fixedRecurringTransfer.remove();
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
                int size = new MultiParent_Set<>(FixedRecurringTransfer.class, period, this).get().size();
                if(size > 1){
                    throw new RuntimeException("Duplicate payment");
                }
                if(size == 0){
                    new FixedRecurringTransfer(TrackingDatabase.get().getNextId(), getValue(), period, getBank(), getCategory(), this).add();
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
