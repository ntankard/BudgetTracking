package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class NonPeriodFund {

    // My parents

    // My values
    private String id;

    // My Children
    private List<NonPeriodFundTransfer> nonPeriodFundTransfers = new ArrayList<>();

    /**
     * Constructor
     */
    public NonPeriodFund(String id) {
        this.id = id;
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

    // NonPeriodFundTransfer Link --------------------------------------------------------------------------------------

    /**
     * Notify that a NonPeriodFundTransfer has linked to this Period
     *
     * @param added The NonPeriodFundTransfer that linked
     */
    public void notifyNonPeriodFundTransferLink(NonPeriodFundTransfer added) {
        nonPeriodFundTransfers.add(added);
    }

    /**
     * Notify that a NonPeriodFundTransfer has removed there link to this Period
     *
     * @param removed The NonPeriodFundTransfer that was linked
     */
    public void notifyNonPeriodFundTransferLinkRemove(NonPeriodFundTransfer removed) {
        nonPeriodFundTransfers.remove(removed);
    }

    /**
     * Get all the NonPeriodFundTransfer that have linked to this Period
     *
     * @return All the NonPeriodFundTransfer that have linked to this Period
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<NonPeriodFundTransfer> getNonPeriodFundTransfers() {
        return nonPeriodFundTransfers;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setId(String id) {
        this.id = id;
    }
}
