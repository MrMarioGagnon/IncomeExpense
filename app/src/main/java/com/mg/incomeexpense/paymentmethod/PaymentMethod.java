package com.mg.incomeexpense.paymentmethod;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IdToItemConvertor;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentMethod extends ObjectBase implements Serializable, Comparable<PaymentMethod> {

    private static final String LOG_TAG = PaymentMethod.class.getSimpleName();

    private String mName;
    private String mCurrency;
    private Double mExchangeRate;
    private Boolean mIsClose;
    private Contributor mOwner;

    private PaymentMethod() {

    }

    public static PaymentMethod create(Cursor cursor, ContentResolver contentResolver) {
        PaymentMethod newInstance = new PaymentMethod();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME));
        String currency = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CURRENCY));
        Integer close = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CLOSE));
        Double exchangeRate = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_EXCHANGE_RATE));
        String contributors = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CONTRIBUTORS));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mIsClose = close == 1 ? true : false;
        newInstance.mExchangeRate = exchangeRate;
        List<Contributor> owners = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ";");
        if(owners.size() == 1){
            newInstance.mOwner = owners.get(0);
        }

        return newInstance;
    }

    public static PaymentMethod create(Long id, String name, String currency, Double exchangeRate, Boolean isClose, Contributor owner) {

        PaymentMethod newInstance = new PaymentMethod();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mExchangeRate = exchangeRate;
        newInstance.mIsClose = isClose;
        newInstance.mOwner = owner;

        return newInstance;
    }

    public static PaymentMethod createNew() {

        PaymentMethod newInstance = new PaymentMethod();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mName = "";
        newInstance.mCurrency = "";
        newInstance.mExchangeRate = 1.0;
        newInstance.mIsClose = false;
        newInstance.mOwner = null;

        return newInstance;

    }

    public Contributor getOwner() {
        return mOwner;
    }

    public void setContributors(Contributor owner) {
        if (null == mOwner || !mOwner.equals(owner)) {
            mDirty = true;
            this.mOwner = owner;
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

    public Double getExchangeRate() {
        return mExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate){
        if(!mExchangeRate.equals(exchangeRate)){
            mDirty = true;
            mExchangeRate = exchangeRate;
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

        PaymentMethod that = (PaymentMethod) o;

        if (!mName.equals(that.mName)) return false;
        if (!mCurrency.equals(that.mCurrency)) return false;
        if (!mExchangeRate.equals(that.mExchangeRate)) return false;
        if (!mIsClose.equals(that.mIsClose)) return false;
        return mOwner.equals(that.mOwner);

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mCurrency.hashCode();
        result = 31 * result + mExchangeRate.hashCode();
        result = 31 * result + mIsClose.hashCode();
        result = 31 * result + mOwner.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%1$s(%2$s)", getName(), getCurrency());
    }

    @Override
    public int compareTo(@NonNull PaymentMethod instanceToCompare) {
        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

}
