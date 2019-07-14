package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

public class Currency {

    // My parents

    // My values
    private String id;
    private double toAUD;

    // My children
    private List<Bank> banks = new ArrayList<>();

    /**
     * Constructor
     */
    public Currency(String id, double toAUD) {
        this.id = id;
        this.toAUD = toAUD;
    }

    /**
     * Notify that another object has linked to this one
     * @param added The object that linked
     */
    public void notifyBankLink(Bank added){
        banks.add(added);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId(){
        return id;
    }
    public double getToAUD() {
        return toAUD;
    }

    public List<Bank> getBanks() {
        return banks;
    }
}