package com.mg.incomeexpense.account;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IdToItemConvertor;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class Account extends ObjectBase implements Serializable, Comparable<Account> {

    private String mName;
    private List<Contributor> mContributors;
    private List<Category> mCategories;
    private Double mBudget;
    private Boolean mIsClose;

    private Account() {

    }

    public static Account create(@NonNull Cursor cursor, @NonNull ContentResolver contentResolver) {

        Objects.requireNonNull(cursor, "Parameter cursor of type Cursor is mandatory.");
        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory.");

        Account newInstance = new Account();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_NAME));
        Integer close = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE));
        String contributors = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS));
        String categories = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CATEGORIES));
        Double budget = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_BUDGET));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mIsClose = close == 1 ? true : false;
        newInstance.mContributors = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ApplicationConstant.storageSeparator);
        newInstance.mCategories = IdToItemConvertor.ConvertIdsToCategories(contentResolver, IncomeExpenseContract.CategoryEntry.CONTENT_URI, categories, ApplicationConstant.storageSeparator);
        newInstance.mBudget = null == budget ? 0d : budget;

        return newInstance;
    }

    public static Account create(@NonNull Long id, @NonNull String name, Boolean isClose, @NonNull List<Contributor> contributors, Double budget, @NonNull List<Category> categories) {

        Objects.requireNonNull(id, "Parameter id of type Long is mandatory");
        Objects.requireNonNull(name, "Parameter name of type String is mandatory");
        Objects.requireNonNull(contributors, "Parameter contributors of type List<Contributor> is mandatory");

        if (contributors.size() == 0)
            throw new IllegalArgumentException("The size of the list of contributor must be greater than zero");

        Objects.requireNonNull(categories, "Parameter categories of type List<String> is mandatory");

        if (categories.size() == 0)
            throw new IllegalArgumentException("The size of the list of category must be greater than zero");

        Account newInstance = new Account();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mIsClose = null == isClose ? false : isClose;
        newInstance.mContributors = contributors;
        newInstance.mCategories = categories;
        newInstance.mBudget = null == budget ? 0d : budget;

        return newInstance;
    }

    public static Account createNew() {

        Account newInstance = new Account();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mName = "";
        newInstance.mIsClose = false;
        newInstance.mContributors = new ArrayList<>();
        newInstance.mCategories = new ArrayList<>();
        newInstance.mBudget = 0d;

        return newInstance;

    }

    public List<Contributor> getContributors() {
        return mContributors;
    }

    public void setContributors(@NonNull List<Contributor> contributors) {

        Objects.requireNonNull(contributors, "Parameter contributors of type List<Contributor> is mandatory");

        if (!mContributors.equals(contributors)) {
            mDirty = true;
            this.mContributors = contributors;
        }

    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(@NonNull List<Category> categories) {

        Objects.requireNonNull(categories, "Parameter categories of type List<String> is mandatory");

        if (!mCategories.equals(categories)) {
            mDirty = true;
            this.mCategories = categories;
        }

    }

    public String getName() {

        return mName;
    }

    public void setName(@NonNull String name) {

        Objects.requireNonNull(name, "Parameter name of type String is mandatory");

        if (!mName.equals(name)) {
            mDirty = true;
            mName = name;
        }

    }

    public Double getBudget() {
        return mBudget;
    }

    public void setBudget(Double budget) {

        if (null == budget)
            budget = 0d;

        if (!mBudget.equals(budget)) {
            mDirty = true;
            mBudget = budget;
        }
    }

    public String getBudgetAsString(){
        return Tools.formatAmount(getBudget());
    }

    public Boolean getIsClose() {
        return mIsClose;
    }

    public void setIsClose(Boolean isClose) {

        if (null == isClose)
            isClose = false;

        if (!mIsClose.equals(isClose)) {
            mDirty = true;
            mIsClose = isClose;
        }
    }

    @Override
    public boolean equals(Object o) {

        if (null == o) return false;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!mName.equals(account.mName)) return false;
        if (!mContributors.equals(account.mContributors)) return false;
        if (!mCategories.equals(account.mCategories)) return false;
        if (mBudget != null ? !mBudget.equals(account.mBudget) : account.mBudget != null)
            return false;
        return mIsClose.equals(account.mIsClose);

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mContributors.hashCode();
        result = 31 * result + mCategories.hashCode();
        result = 31 * result + (mBudget != null ? mBudget.hashCode() : 0);
        result = 31 * result + mIsClose.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%1$s", getName());
    }

    @Override
    public int compareTo(@NonNull Account instanceToCompare) {

        Objects.requireNonNull(instanceToCompare, "Parameter instanceToCompare of type Account is mandatory");

        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

    public String getContributorsForDisplay() {
        return Tools.join(mContributors, ApplicationConstant.displaySeparator);
    }

    public String getContributorsIds() {
        List<String> a = new ArrayList<>();
        for (Contributor item : mContributors) {
            a.add(item.getId().toString());
        }
        return Tools.join(a, ApplicationConstant.storageSeparator);
    }

    public String getCategoriesAsIds() {
        List<String> a = new ArrayList<>();
        for (Category item : mCategories) {
            a.add(item.getId().toString());
        }
        return Tools.join(a, ApplicationConstant.storageSeparator);
    }

}