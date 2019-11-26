package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CurrencyBankTransfer extends BankTransfer {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination"})
    public CurrencyBankTransfer(Integer id, String description, Double value, Period period, Bank source, Bank destination) {
        super(id, description, value, period, source, destination);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Destination")) {
            List<T> toReturn = getSource().getCurrency().getChildren(type);
            toReturn.remove(getSource());
            return toReturn;
        }
        if (fieldName.equals("Source")) {
            List<T> toReturn = new ArrayList<>(super.sourceOptions(type, fieldName));
            toReturn.remove(getDestination());
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        return super.getValue();
    }

    @Override
    @DisplayProperties(order = 5)
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Currency getCurrency() {
        return getSource().getCurrency();
    }

}
