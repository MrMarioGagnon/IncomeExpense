package com.mg.incomeexpense.transaction;

import android.content.ContentResolver;
import android.database.Cursor;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.io.Serializable;

/**
 * Created by mario on 2016-07-23.
 */
public class Transaction extends ObjectBase implements Serializable, Comparable<Transaction> {

    private static final String LOG_TAG = Transaction.class.getSimpleName();

    private Account mAccount;
    private Category mCategory;
    private TransactionType mType;
    private String mDate;
    private Double mAmount;
    private String mCurrency;
    private Double mExchangeRate;
    private PaymentMethod mPaymentMethod;
    private String mNote;

    private Transaction() {

    }

    public static Transaction create(Cursor cursor, ContentResolver contentResolver) {
        Transaction newInstance = new Transaction();
        newInstance.mNew = false;
        newInstance.mDirty = false;

//        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_ID));
//        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_NAME));
//        String currency = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CURRENCY));
//        Integer close = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE));
//        String contributors = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS));
//
//        newInstance.mId = id;
//        newInstance.mName = name;
//        newInstance.mCurrency = currency;
//        newInstance.mIsClose = close == 1 ? true : false;
//        newInstance.mContributors = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ";");

        return newInstance;
    }

    public static Transaction createNew() {

        Transaction newInstance = new Transaction();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mAccount = null;
        newInstance.mCategory = null;
        newInstance.mType = null;
        newInstance.mDate = Tools.now();
        newInstance.mAmount = 0.0;
        newInstance.mCurrency = "";
        newInstance.mExchangeRate = 1.0;
        newInstance.mPaymentMethod = null;
        newInstance.mNote = "";

        return newInstance;

    }

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        if (!mAccount.equals(account)) {
            mDirty = true;
            mAccount = account;
        }
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        if (!mAmount.equals(amount)) {
            mDirty = true;
            mAmount = amount;
        }
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        if (!mCategory.equals(category)) {
            mDirty = true;
            mCategory = category;
        }
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        if (!mCurrency.equals(currency)) {
            mDirty = true;
            mCurrency = currency;
        }
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        if (!mDate.equals(date)) {
            mDirty = true;
            mDate = date;
        }
    }

    public Double getExchangeRate() {
        return mExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        if (!mExchangeRate.equals(exchangeRate)) {
            mDirty = true;
            mExchangeRate = exchangeRate;
        }
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        if (!mNote.equals(note)) {
            mDirty = true;
            mNote = note;
        }
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (!mPaymentMethod.equals(paymentMethod)) {
            mDirty = true;
            mPaymentMethod = paymentMethod;
        }
    }

    public TransactionType getType() {
        return mType;
    }

    public void setType(TransactionType type) {
        if (!mType.equals(type)) {
            mDirty = true;
            mType = type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!mAccount.equals(that.mAccount)) return false;
        if (!mCategory.equals(that.mCategory)) return false;
        if (mType != that.mType) return false;
        if (!mDate.equals(that.mDate)) return false;
        if (!mAmount.equals(that.mAmount)) return false;
        if (!mCurrency.equals(that.mCurrency)) return false;
        if (!mExchangeRate.equals(that.mExchangeRate)) return false;
        if (!mPaymentMethod.equals(that.mPaymentMethod)) return false;
        return mNote != null ? mNote.equals(that.mNote) : that.mNote == null;

    }

    @Override
    public int hashCode() {
        int result = mAccount.hashCode();
        result = 31 * result + mCategory.hashCode();
        result = 31 * result + mType.hashCode();
        result = 31 * result + mDate.hashCode();
        result = 31 * result + mAmount.hashCode();
        result = 31 * result + mCurrency.hashCode();
        result = 31 * result + mExchangeRate.hashCode();
        result = 31 * result + mPaymentMethod.hashCode();
        result = 31 * result + (mNote != null ? mNote.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Transaction another) {
        return 0;
    }

    public enum TransactionType {
        Expense, Income
    }


}