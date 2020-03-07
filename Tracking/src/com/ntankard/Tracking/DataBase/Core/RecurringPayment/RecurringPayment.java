package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public abstract class RecurringPayment extends NamedDataObject implements CurrencyBound {

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
    @ParameterMap(shouldSave = false)
    public RecurringPayment(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, Category category) {
        super(id, name);
        if (value == null) throw new IllegalArgumentException("Value is null");
        if (start == null) throw new IllegalArgumentException("Start is null");
        if (bank == null) throw new IllegalArgumentException("Bank is null");
        if (category == null) throw new IllegalArgumentException("Category is null");
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
    @DisplayProperties(order = 2000000)
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
     * Create all the children transactions
     */
    public abstract void regenerateChildren();

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("End")) {
            List<T> all = super.sourceOptions(type, fieldName);
            all.remove(getStart());
            all.removeIf(t -> {
                ExistingPeriod existingPeriod = (ExistingPeriod) t;
                return existingPeriod.getOrder() <= start.getOrder();
            });
            all.add(null);
            return all;
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000--getName

    @DisplayProperties(order = 1110000)
    public ExistingPeriod getStart() {
        return start;
    }

    @DisplayProperties(order = 1120000)
    public ExistingPeriod getEnd() {
        return end;
    }

    @DisplayProperties(order = 1130000)
    public Bank getBank() {
        return bank;
    }

    @DisplayProperties(order = 1140000)
    public Category getCategory() {
        return category;
    }

    @DisplayProperties(order = 1150000, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1160000)
    @Override
    public Currency getCurrency() {
        return getBank().getCurrency();
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

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
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category is null");

        this.category.notifyChildUnLink(this);
        this.category = category;
        this.category.notifyChildLink(this);
        regenerateChildren();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        if (start == null) throw new IllegalArgumentException("Start is null");

        this.start.notifyChildUnLink(this);
        this.start = start;
        this.start.notifyChildLink(this);

        if (getEnd() != null) {
            if (getStart().getOrder() >= getEnd().getOrder()) {
                setEnd(sourceOptions(ExistingPeriod.class, "End").get(0));
            }
        }

        regenerateChildren();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setEnd(ExistingPeriod end) {
        if (end != null && (getStart().getOrder() >= end.getOrder())) {
            throw new IllegalArgumentException("Setting an end date before the start");
        }
        if (this.end != null) {
            this.end.notifyChildUnLink(this);
        }
        this.end = end;
        if (this.end != null) {
            this.end.notifyChildLink(this);
        }
        regenerateChildren();
        validateParents();
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
