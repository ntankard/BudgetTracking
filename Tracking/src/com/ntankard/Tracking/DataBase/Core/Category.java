package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class Category {

    // My parents
    private Category idCategory;

    // My values
    private String idName;

    // My Children
    private List<Category> categories = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<CategoryTransfer> categoriesTransferSources = new ArrayList<>();
    private List<CategoryTransfer> categoriesTransferDestinations = new ArrayList<>();

    /**
     * Constructor
     */
    public Category(String idName, Category idCategory) {
        this.idName = idName;
        this.idCategory = idCategory;
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

    // Category Link ---------------------------------------------------------------------------------------------------

    /**
     * Notify that a Category has linked to this Category
     *
     * @param added The Category that linked
     */
    public void notifyCategoryLink(Category added) {
        categories.add(added);
    }

    /**
     * Notify that a Category has removed there link to this Category
     *
     * @param removed The Category that was linked
     */
    public void notifyCategoryLinkRemove(Category removed) {
        categories.remove(removed);
    }

    /**
     * Get all the Categorys that have linked to this Category
     *
     * @return All the Categorys that have linked to this Category
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Category> getCategories() {
        return categories;
    }

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
    public void notifyCategoriesTransferDestinationRemove(CategoryTransfer removed) {
        categoriesTransferDestinations.remove(removed);
    }

    /**
     * Get all the CategoryTransfers that have linked to this Category
     *
     * @return All the CategoryTransfers that have linked to this Category
     */
    public List<CategoryTransfer> getCategoriesTransferDestinations() {
        return categoriesTransferDestinations;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId() {
        if (getIdCategory() != null) {
            return getIdCategory().toString() + "-" + getIdName();
        }
        return getIdName();
    }

    public Category getIdCategory() {
        return idCategory;
    }

    public String getIdName() {
        return idName;
    }
}
