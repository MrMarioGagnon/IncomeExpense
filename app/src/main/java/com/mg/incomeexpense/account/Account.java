package com.mg.incomeexpense.account;

import android.content.ContentResolver;
import android.database.Cursor;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IdToItemConvertor;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mario on 2016-07-23.
 */
public class Account extends ObjectBase implements Serializable, Comparable<Account> {

    private String mName;
    private List<Contributor> mContributors;
    private List<String> mCategories;
    private Double mBudget;
    private Boolean mIsClose;

    private Account() {

    }

    public static Account create(Cursor cursor, ContentResolver contentResolver) {
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
        newInstance.mContributors = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ";");
        newInstance.mCategories = Tools.split(categories, ";");
        newInstance.mBudget = budget;

        return newInstance;
    }

    public static Account create(Long id, String name, Boolean isClose, List<Contributor> contributors, Double budget, List<String> categories) {

        Account newInstance = new Account();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mIsClose = isClose;
        newInstance.mContributors = contributors;
        newInstance.mCategories = categories;
        newInstance.mBudget = budget;

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
        newInstance.mBudget = null;

        return newInstance;

    }

    public List<Contributor> getContributors() {
        return mContributors;
    }

    public void setContributors(List<Contributor> contributors) {
        Contributor[] a1 = new Contributor[mContributors.size()];
        mContributors.toArray(a1);
        Contributor[] a2 = new Contributor[contributors.size()];
        contributors.toArray(a2);

        if (!Arrays.equals(a1, a2)) {
            mDirty = true;
            this.mContributors = contributors;
        }
    }

    public List<String> getCategories() {
        return mCategories;
    }

    public void setCategories(List<String> categories) {
        String[] a1 = new String[mCategories.size()];
        mCategories.toArray(a1);
        String[] a2 = new String[categories.size()];
        categories.toArray(a2);

        if (!Arrays.equals(a1, a2)) {
            mDirty = true;
            this.mCategories = categories;
        }

    }

    public String getName() {

        return null == mName ? "" : mName;
    }

    public void setName(String name) {

        if (!mName.equals(name)) {
            mDirty = true;
            mName = name;
        }

    }

    public Double getBudget() {
        return mBudget;
    }

    public void setBudget(Double budget) {
        if (null == mBudget || !mBudget.equals(budget)) {
            mDirty = true;
            mBudget = budget;
        }
    }

    public Boolean getIsClose() {
        return mIsClose;
    }

    public void setIsClose(Boolean isClose) {

        if (!mIsClose.equals(isClose)) {
            mDirty = true;
            mIsClose = isClose;
        }
    }

    @Override
    public boolean equals(Object o) {
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
    public int compareTo(Account instanceToCompare) {
        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

    public String getContributorsForDisplay() {
        return Tools.join(mContributors, ",");
    }

    public String getContributorsIds() {
        List<String> a = new ArrayList<>();
        for (Contributor item : mContributors) {
            a.add(item.getId().toString());
        }
        return Tools.join(a, ";");
    }

}