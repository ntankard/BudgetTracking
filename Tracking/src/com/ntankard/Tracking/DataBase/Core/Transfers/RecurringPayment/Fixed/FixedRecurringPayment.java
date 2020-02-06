package com.ntankard.Tracking.DataBase.Core.Transfers.RecurringPayment.Fixed;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.RecurringPayment.RecurringPayment;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FixedRecurringPayment extends RecurringPayment implements CurrencyBound {

    // My parents
    private ExistingPeriod start;
    private ExistingPeriod end;
    private Bank bank;
    private Category category;

    // My values
    private Double value;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getValue", "getStart", "getEnd", "getBank", "getCategory"})
    public FixedRecurringPayment(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, Category category) {
        super(id, name);
        this.value = value;
        this.start = start;
        this.end = end;
        this.bank = bank;
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getBank());
        toReturn.add(getCategory());
        toReturn.add(getStart());
        if (getEnd() != null) {
            toReturn.add(getEnd());
        }
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        regenerateChildren();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("End")) {
            List<T> all = super.sourceOptions(type, fieldName);
            all.remove(getStart());
            return all;
        }
        return super.sourceOptions(type, fieldName);
    }

    /**
     * Create all the children transactions
     */
    public void regenerateChildren() {
        for (FixedRecurringTransfer fixedRecurringTransfer : getChildren(FixedRecurringTransfer.class)) {
            fixedRecurringTransfer.remove();
        }

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (period.getOrder() >= start.getOrder()) {
                if (end != null) {
                    if (end.getOrder() < period.getOrder()) {
                        continue;
                    }
                }
                new FixedRecurringTransfer(TrackingDatabase.get().getNextId(), period, bank, category, this).add();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 3)
    public ExistingPeriod getStart() {
        return start;
    }

    @DisplayProperties(order = 4)
    public ExistingPeriod getEnd() {
        return end;
    }

    @DisplayProperties(order = 5)
    public Bank getBank() {
        return bank;
    }

    @DisplayProperties(order = 6)
    public Category getCategory() {
        return category;
    }

    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 8)
    @Override
    public Currency getCurrency() {
        return getBank().getCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setBank(Bank bank) {
        if (bank == null) throw new IllegalArgumentException("Bank is null");

        this.bank.notifyChildUnLink(this);
        this.bank = bank;
        this.bank.notifyChildLink(this);
        regenerateChildren();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category is null");

        this.category.notifyChildUnLink(this);
        this.category = category;
        this.category.notifyChildLink(this);
        regenerateChildren();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        if (start == null) throw new IllegalArgumentException("Start is null");

        this.start.notifyChildUnLink(this);
        this.start = start;
        this.start.notifyChildLink(this);

        regenerateChildren();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setEnd(ExistingPeriod end) {
        if (this.end != null) {
            this.end.notifyChildUnLink(this);
        }
        this.end = end;
        if (this.end != null) {
            this.end.notifyChildLink(this);
        }
        regenerateChildren();
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
