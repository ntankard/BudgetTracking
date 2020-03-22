package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

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
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = FundTransfer.getFields();
        toReturn.add(new Field<>("getValue", Double.class));
        return toReturn;
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static ManualFundTransfer make(Integer id, String description, Period period, FundEvent source, Double value, Currency currency) {
        return assembleDataObject(ManualFundTransfer.getFields(), new ManualFundTransfer()
                , "getId", id
                , "getDescription", description
                , "getPeriod", period
                , "getSource", source
                , "getValue", value
                , "getCurrency", currency
        );
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
        return get("getValue");
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
        set("getValue", value);
        updateHalfTransfer();
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Pool source) {
        super.setSource(source);
    }
}
