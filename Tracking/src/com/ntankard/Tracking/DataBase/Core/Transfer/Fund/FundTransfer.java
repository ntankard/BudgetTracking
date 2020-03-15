package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class FundTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String description,
                                           Period period, FundEvent source, Currency currency, DataObject container) {
        List<Field<?>> toReturn = Transfer.getFields(id, description, period, source, container);
        toReturn.add(new Field<>("description", String.class, description, container));
        toReturn.add(new DataObject_Field<>("destination", Pool.class, source.getCategory(), container));
        toReturn.add(new DataObject_Field<>("currency", Currency.class, currency, container));
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            List<T> toReturn = new ArrayList<>();
            for (FundEvent fundEvent : super.sourceOptions(FundEvent.class, fieldName)) {
                toReturn.add((T) fundEvent);
            }
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getDescription
    // 1200000----getPeriod

    @Override
    @DisplayProperties(order = 1300000)
    public Pool getSource() {
        return super.getSource();
    }

    // 1400000----getValue

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getCurrency() {
        return get("currency");
    }

    @Override
    @DisplayProperties(order = 1600000)
    public Pool getDestination() {
        return get("destination");
    }

    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected void setSource(Pool fund) {
        if (!(fund instanceof FundEvent)) throw new IllegalArgumentException("Source is not a fundEvent");
        super.setSource(fund);
    }

    public void setDestination() {
        Pool destination = ((FundEvent) getSource()).getCategory();
        set("destination", destination);
        updateHalfTransfer();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCurrency(Currency currency) {
        set("currency", currency);
        updateHalfTransfer();
        validateParents();
    }
}
