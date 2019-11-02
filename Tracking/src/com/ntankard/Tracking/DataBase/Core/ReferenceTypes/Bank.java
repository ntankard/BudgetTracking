package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Bank extends DataObject implements CurrencyBound {

    // My parents
    private Currency currency;

    // My values
    private String bank;
    private String account;
    private int order;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getBank", "getAccount", "getCurrency", "getOrder"})
    public Bank(String id, String bank, String account, Currency currency, int order) {
        super(id);
        this.bank = bank;
        this.account = account;
        this.currency = currency;
        this.order = order;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getBank() + "-" + getAccount();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(currency);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 3)
    public String getBank() {
        return bank;
    }

    @DisplayProperties(order = 4)
    public String getAccount() {
        return account;
    }

    @DisplayProperties(order = 5)
    public Currency getCurrency() {
        return currency;
    }

    @DisplayProperties(order = 6)
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public int getOrder() {
        return order;
    }
}
