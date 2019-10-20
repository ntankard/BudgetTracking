package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Bank extends DataObject implements CurrencyBound {

    // My parents
    private Currency currency;

    // My values
    private String idBank;
    private String idAccount;
    private int order;

    /**
     * Constructor
     */
    public Bank(String idBank, String idAccount, Currency currency, int order) {
        this.idBank = idBank;
        this.idAccount = idAccount;
        this.currency = currency;
        this.order = order;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public String getId() {
        return getIdBank() + "-" + getIdAccount();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(currency);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getIdBank() {
        return idBank;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public int getOrder() {
        return order;
    }
}
