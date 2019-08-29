package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

public class Currency {

    // My parents

    // My values
    private String id;
    private double toSecondary;
    private double toPrimary;
    private boolean isPrimary;

    // My children
    private List<Bank> banks = new ArrayList<>();

    /**
     * Constructor
     */
    public Currency(String id, double toSecondary, double toPrimary, boolean isPrimary) {
        this.id = id;
        this.toSecondary = toSecondary;
        this.toPrimary = toPrimary;
        this.isPrimary = isPrimary;
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param added The object that linked
     */
    public void notifyBankLink(Bank added) {
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

    public String getId() {
        return id;
    }

    public double getToSecondary() {
        return toSecondary;
    }

    public double getToPrimary() {
        return toPrimary;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public List<Bank> getBanks() {
        return banks;
    }
}