package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class ManualFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String description,
                                           Period period, FundEvent source, Double value, Currency currency, DataObject container) {
        List<Field<?>> toReturn = FundTransfer.getFields(id, description, period, source, currency, container);
        toReturn.add(new Field<>("value", Double.class, value, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getPeriod", "getSource", "getValue", "getCurrency"})
    public ManualFundTransfer(Integer id, String description,
                              Period period, FundEvent source, Double value, Currency currency) {
        super();
        setFields(getFields(id, description, period, source, value, currency, this));
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getDescription
    // 1200000----getPeriod
    // 1300000----getSource

    @Override
    @DisplayProperties(order = 1400000, dataType = CURRENCY)
    public Double getValue() {
        return get("value");
    }

    // 1500000----getCurrency
    // 1600000----getDestination
    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setValue(Double value) {
        set("value", value);
        updateHalfTransfer();
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Pool source) {
        super.setSource(source);
    }
}
