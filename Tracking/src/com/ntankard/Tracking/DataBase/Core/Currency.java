package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

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
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Link Management ################################################
    //------------------------------------------------------------------------------------------------------------------

    // Bank Link -------------------------------------------------------------------------------------------------------

    /**
     * Notify that a Bank has linked to this Currency
     *
     * @param added The Bank that linked
     */
    public void notifyBankLink(Bank added) {
        banks.add(added);
    }

    /**
     * Notify that a Bank has removed there link to this Currency
     *
     * @param removed The Bank that was linked
     */
    public void notifyBankLinkRemove(Bank removed) {
        banks.remove(removed);
    }

    /**
     * Get all the Banks that have linked to this Currency
     *
     * @return All the Banks that have linked to this Currency
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Bank> getBanks() {
        return banks;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
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
}
