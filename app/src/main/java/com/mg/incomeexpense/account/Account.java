package com.mg.incomeexpense.account;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

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

    private static final String LOG_TAG = Account.class.getSimpleName();

    private String mName;

    private String mCurrency;
    private Boolean mIsClose;
    private List<Contributor> mContributors;

    private Account() {

    }

    public static Account create(Cursor cursor, ContentResolver contentResolver) {
        Account newInstance = new Account();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_NAME));
        String currency = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CURRENCY));
        Integer close = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE));
        String contributors = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mIsClose = close == 1 ? true : false;
        newInstance.mContributors = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ";");

        return newInstance;
    }

    public static Account create(Long id, String name, String currency, Boolean isClose, List<Contributor> contributors) {

        Account newInstance = new Account();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mIsClose = isClose;
        newInstance.mContributors = contributors;

        return newInstance;
    }

    public static Account createNew() {

        Account newInstance = new Account();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mName = "";
        newInstance.mCurrency = "";
        newInstance.mIsClose = false;
        newInstance.mContributors = new ArrayList<>();

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

    public String getName() {

        return null == mName ? "" : mName;
    }

    public void setName(String name) {

        if (!mName.equals(name)) {
            mDirty = true;
            mName = name;
        }

    }

    public String getCurrency() {
        return null == mCurrency ? "" : mCurrency;
    }

    public void setCurrency(String currency) {

        if (!mCurrency.equals(currency)) {
            mDirty = true;
            mCurrency = currency;
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
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (!mId.equals(account.mId)) return false;
        if (!mName.equals(account.mName)) return false;
        if (!mCurrency.equals(account.mCurrency)) return false;
        return mIsClose.equals(account.mIsClose);

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + mCurrency.hashCode();
        result = 31 * result + mIsClose.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%1$s(%2$s)", getName(), getCurrency());
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