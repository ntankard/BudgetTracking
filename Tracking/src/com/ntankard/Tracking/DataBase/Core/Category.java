package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

public class Category {
    // My parents
    private Category idCategory;

    // My values
    private String idName;

    // My Children
    private List<Category> categories = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Constructor
     */
    public Category(String idName, Category idCategory) {
        this.idCategory = idCategory;
        this.idName = idName;
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param category The object that linked
     */
    public void notifyCategoryLink(Category category) {
        categories.add(category);
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param transaction The object that linked
     */
    public void notifyTransactionLink(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Notify that another object has removed there link to this one
     *
     * @param category The object was linked
     */
    public void notifyCategoryLinkRemove(Category category) {
        categories.remove(category);
    }

    /**
     * Notify that another object has removed there link to this one
     *
     * @param transaction The object was linked
     */
    public void notifyTransactionLinkRemove(Transaction transaction) {
        transactions.remove(transaction);
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
        if (idCategory != null) {
            return idCategory.toString() + "-" + idName;
        }
        return idName;
    }

    public Category getIdCategory() {
        return idCategory;
    }

    public String getIdName() {
        return idName;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setIdCategory(Category idCategory) {
        if (this.idCategory != null) {
            this.idCategory.notifyCategoryLinkRemove(this);
        }

        this.idCategory = idCategory;
        this.idCategory.notifyCategoryLink(this);
    }
}
