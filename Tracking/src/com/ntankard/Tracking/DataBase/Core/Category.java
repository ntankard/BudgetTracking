package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.PeriodTransfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class Category {

    // My parents

    // My values
    private String id;
    private int order;

    // My Children
    private List<Transaction> transactions = new ArrayList<>();
    private List<CategoryTransfer> categoriesTransferSources = new ArrayList<>();
    private List<CategoryTransfer> categoriesTransferDestinations = new ArrayList<>();
    private List<PeriodTransfer> periodTransfers = new ArrayList<>();
    private List<NonPeriodFundTransfer> nonPeriodFundTransfers = new ArrayList<>();

    /**
     * Constructor
     */
    public Category(String id, int order) {
        this.id = id;
        this.order = order;
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

    // Transaction Link ------------------------------------------------------------------------------------------------

    /**
     * Notify that a Transaction has linked to this Category
     *
     * @param added The Transaction that linked
     */
    public void notifyTransactionLink(Transaction added) {
        transactions.add(added);
    }

    /**
     * Notify that a Transaction has removed there link to this Category
     *
     * @param removed The Transaction that was linked
     */
    public void notifyTransactionLinkRemove(Transaction removed) {
        transactions.remove(removed);
    }

    /**
     * Get all the Transactions that have linked to this Category
     *
     * @return All the Transactions that have linked to this Category
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // CategoryTransfer Source Link ------------------------------------------------------------------------------------

    /**
     * Notify that a CategoryTransfer has linked to this Category
     *
     * @param added The CategoryTransfer that linked
     */
    public void notifyCategoriesTransferSourceLink(CategoryTransfer added) {
        categoriesTransferSources.add(added);
    }

    /**
     * Notify that a CategoryTransfer has removed there link to this Category
     *
     * @param removed The CategoryTransfer that was linked
     */
    public void notifyCategoriesTransferSourceLinkRemove(CategoryTransfer removed) {
        categoriesTransferSources.remove(removed);
    }

    /**
     * Get all the CategoryTransfers that have linked to this Category
     *
     * @return All the CategoryTransfers that have linked to this Category
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<CategoryTransfer> getCategoriesTransferSources() {
        return categoriesTransferSources;
    }

    // CategoryTransfer Destination Link -------------------------------------------------------------------------------

    /**
     * Notify that a CategoryTransfer has linked to this Category
     *
     * @param added The CategoryTransfer that linked
     */
    public void notifyCategoriesTransferDestinationLink(CategoryTransfer added) {
        categoriesTransferDestinations.add(added);
    }

    /**
     * Notify that a CategoryTransfer has removed there link to this Category
     *
     * @param removed The CategoryTransfer that was linked
     */
    public void notifyCategoriesTransferDestinationLinkRemove(CategoryTransfer removed) {
        categoriesTransferDestinations.remove(removed);
    }

    /**
     * Get all the CategoryTransfers that have linked to this Category
     *
     * @return All the CategoryTransfers that have linked to this Category
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<CategoryTransfer> getCategoriesTransferDestinations() {
        return categoriesTransferDestinations;
    }

    // PeriodTransfer Link ---------------------------------------------------------------------------------------------

    /**
     * Notify that a PeriodTransfer has linked to this Period
     *
     * @param added The PeriodTransfer that linked
     */
    public void notifyPeriodTransferLink(PeriodTransfer added) {
        periodTransfers.add(added);
    }

    /**
     * Notify that a PeriodTransfer has removed there link to this Period
     *
     * @param removed The PeriodTransfer that was linked
     */
    public void notifyPeriodTransferLinkRemove(PeriodTransfer removed) {
        periodTransfers.remove(removed);
    }

    /**
     * Get all the PeriodTransfer that have linked to this Period
     *
     * @return All the PeriodTransfer that have linked to this Period
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<PeriodTransfer> getPeriodTransfers() {
        return periodTransfers;
    }

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

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public int getOrder() {
        return order;
    }
}
