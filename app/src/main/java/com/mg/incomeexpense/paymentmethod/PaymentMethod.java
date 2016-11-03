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
import java.util.Objects;

public class PaymentMethod extends ObjectBase implements Serializable, Comparable<PaymentMethod> {

    private String mName;
    private String mCurrency;
    private Double mExchangeRate;
    private Boolean mIsClose;
    private Contributor mOwner;

    private PaymentMethod() {

    }

    public static PaymentMethod create(@NonNull Cursor cursor, @NonNull ContentResolver contentResolver) {

        Objects.requireNonNull(cursor, "Parameter cursor of type Cursor is mandatory.");
        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory.");

        PaymentMethod newInstance = new PaymentMethod();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME));
        String currency = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CURRENCY));
        Integer close = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CLOSE));
        Double exchangeRate = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_EXCHANGE_RATE));
        Long ownerId = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_OWNER_ID));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mIsClose = close == 1 ? true : false;
        newInstance.mExchangeRate = exchangeRate;
        newInstance.mOwner = IdToItemConvertor.ConvertIdToContributor(contentResolver, ownerId);

        return newInstance;
    }

    public static PaymentMethod create(@NonNull Long id, @NonNull String name, @NonNull String currency, Double exchangeRate, Boolean isClose, @NonNull Contributor owner) {

        Objects.requireNonNull(id, "Parameter id of type Long is mandatory");
        Objects.requireNonNull(name, "Parameter name of type String is mandatory");
        Objects.requireNonNull(currency, "Parameter currency of type String is mandatory");
        Objects.requireNonNull(owner, "Parameter owner of type Contributor is mandatory");

        PaymentMethod newInstance = new PaymentMethod();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mCurrency = currency;
        newInstance.mExchangeRate = null == exchangeRate ? 1.0 : exchangeRate;
        newInstance.mIsClose = null == isClose ? false : isClose;
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

    public void setOwner(Contributor owner) {

        if (null == mOwner || !mOwner.equals(owner)) {
            mDirty = true;
            this.mOwner = owner;
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

    public String getCurrency() {
        return null == mCurrency ? "" : mCurrency;
    }

    public void setCurrency(@NonNull String currency) {

        Objects.requireNonNull(currency, "Parameter currency of type String is mandatory");

        if (!mCurrency.equals(currency)) {
            mDirty = true;
            mCurrency = currency;
        }
    }

    public Double getExchangeRate() {
        return mExchangeRate;
    }

    public String getExchangeRateAsString(){
        return Tools.formatAmount(getExchangeRate());
    }

    public void setExchangeRate(Double exchangeRate){

        if(null == exchangeRate)
            exchangeRate = 1.0;

        if(!mExchangeRate.equals(exchangeRate)){
            mDirty = true;
            mExchangeRate = exchangeRate;
        }
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
        if(null == o) return false;
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

        Objects.requireNonNull(instanceToCompare, "Parameter instanceToCompare of type PaymentAccount is mandatory");

        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

}
