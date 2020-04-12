package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.Ordered_FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.SourceDriver.DataObjectField_SourceDriver;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {RePayFundTransfer.class})
public abstract class RecurringPayment extends NamedDataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = NamedDataObject.getFields();
        toReturn.add(new Field<>("getValue", Double.class));
        DataObject_Field<?> bank = new DataObject_Field<>("getBank", Bank.class);
        toReturn.add(bank);
        toReturn.add(new DataObject_Field<>("getCategory", SolidCategory.class));
        toReturn.add(new DataObject_Field<>("getCurrency", Currency.class).addSourceDriver(new DataObjectField_SourceDriver<>(bank, "getCurrency")));

        Field<ExistingPeriod> startField = new DataObject_Field<>("getStart", ExistingPeriod.class);
        Field<ExistingPeriod> endField = new DataObject_Field<>("getEnd", ExistingPeriod.class, true);

        endField.addFilter(new Ordered_FieldFilter<>(startField, Ordered_FieldFilter.OrderSequence.ABOVE));
        startField.addFilter(new Ordered_FieldFilter<>(endField, Ordered_FieldFilter.OrderSequence.BELOW));

        toReturn.add(startField);
        toReturn.add(endField);
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000--getName

    @DisplayProperties(order = 1110000)
    public ExistingPeriod getStart() {
        return get("getStart");
    }

    @DisplayProperties(order = 1120000)
    public ExistingPeriod getEnd() {
        return get("getEnd");
    }

    @DisplayProperties(order = 1130000)
    public Bank getBank() {
        return get("getBank");
    }

    @DisplayProperties(order = 1140000)
    public SolidCategory getCategory() {
        return get("getCategory");
    }

    @DisplayProperties(order = 1150000, dataType = CURRENCY)
    public Double getValue() {
        return get("getValue");
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1160000)
    @Override
    public Currency getCurrency() {
        return get("getCurrency");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override

    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Start")) {
            List<T> all = super.sourceOptions(type, fieldName);
            if (getEnd() != null) {
                all.removeIf(t -> {
                    ExistingPeriod existingPeriod = (ExistingPeriod) t;
                    return existingPeriod.getOrder() >= getEnd().getOrder();
                });
            }
            all.remove(getEnd());
            return all;
        } else if (fieldName.equals("End")) {
            List<T> all = super.sourceOptions(type, fieldName);
            all.remove(getStart());
            all.removeIf(t -> {
                ExistingPeriod existingPeriod = (ExistingPeriod) t;
                return existingPeriod.getOrder() <= getStart().getOrder();
            });
            all.add(null);
            return all;
        }
        return super.sourceOptions(type, fieldName);
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setBank(Bank bank) {
        set("getBank", bank);
        regenerateChildren();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(SolidCategory solidCategory) {
        set("getCategory", solidCategory);
        regenerateChildren();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setStart(ExistingPeriod start) {
        set("getStart", start);
        regenerateChildren();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setEnd(ExistingPeriod end) {
        set("getEnd", end);
        regenerateChildren();
        validateParents();
    }

    public void setValue(Double value) {
        set("getValue", value);
    }
}
