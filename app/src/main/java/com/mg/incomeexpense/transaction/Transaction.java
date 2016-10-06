package com.mg.incomeexpense.transaction;

import android.content.ContentResolver;
import android.database.Cursor;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IdToItemConvertor;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mario on 2016-07-23.
 */
public class Transaction extends ObjectBase implements Serializable, Comparable<Transaction> {

    private static final String LOG_TAG = Transaction.class.getSimpleName();

    private Account mAccount;
    private String mCategory;
    private TransactionType mType;
    private String mDate;
    private Double mAmount;
    private String mCurrency;
    private Double mExchangeRate;
    private PaymentMethod mPaymentMethod;
    private List<Contributor> mContributors;
    private String mNote;

    private Transaction() {

    }

    public static Transaction create(Cursor cursor, ContentResolver contentResolver) {
        Transaction newInstance = new Transaction();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_ID));
        Long accountId = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID));
        String category = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY));
        int type = cursor.getInt(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_TYPE));
        String date = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_DATE));
        Double amount = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT));
        String currency = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY));
        Double exchangeRate = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE));
        Long paymentMethodId = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID));
        String contributors = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS));
        String note = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_NOTE));

        Cursor subItemCursor = contentResolver.query(IncomeExpenseContract.AccountEntry.buildInstanceUri(accountId), null, null, null, null);
        Account account = null;
        if (subItemCursor != null) {
            if (subItemCursor.moveToFirst()) {
                account = Account.create(subItemCursor, contentResolver);
            }
        }

        subItemCursor = contentResolver.query(IncomeExpenseContract.PaymentMethodEntry.buildInstanceUri(paymentMethodId), null, null, null, null);
        PaymentMethod paymentMethod = null;
        if (subItemCursor != null) {
            if (subItemCursor.moveToFirst()) {
                paymentMethod = PaymentMethod.create(subItemCursor, contentResolver);
            }
        }

        newInstance.mId = id;
        newInstance.mAccount = account;
        newInstance.mCategory = category;
        newInstance.mType = type == 0 ? TransactionType.Expense : TransactionType.Income;
        newInstance.mDate = date;
        newInstance.mAmount = amount;
        newInstance.mCurrency = currency;
        newInstance.mExchangeRate = exchangeRate;
        newInstance.mPaymentMethod = paymentMethod;
        newInstance.mContributors = IdToItemConvertor.ConvertIdsToContributors(contentResolver, IncomeExpenseContract.ContributorEntry.CONTENT_URI, contributors, ";");
        newInstance.mNote = note;

        return newInstance;
    }

    public static Transaction createNew() {

        Transaction newInstance = new Transaction();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mAccount = null;
        newInstance.mCategory = null;
        newInstance.mType = TransactionType.Expense;
        newInstance.mDate = Tools.now();
        newInstance.mAmount = 0.0;
        newInstance.mCurrency = "";
        newInstance.mExchangeRate = 1.0;
        newInstance.mPaymentMethod = null;
        newInstance.mContributors = new ArrayList<>();
        newInstance.mNote = "";

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


    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        if (mAccount == null || !mAccount.equals(account)) {
            mDirty = true;
            mAccount = account;
        }
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        if (mAmount == null || !mAmount.equals(amount)) {
            mDirty = true;
            mAmount = amount;
        }
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        if (mCategory == null || !mCategory.equals(category)) {
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

    public int getDateAsInt() {
        return Integer.parseInt(mDate.replace("-", ""));
    }

    public Double getExchangeRate() {
        return mExchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        if (mExchangeRate == null || !mExchangeRate.equals(exchangeRate)) {
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
        if (mPaymentMethod == null || !mPaymentMethod.equals(paymentMethod)) {
            mDirty = true;
            mPaymentMethod = paymentMethod;
        }
    }

    public TransactionType getType() {
        return mType;
    }

    public void setType(TransactionType type) {
        if (mType == null || !mType.equals(type)) {
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
        if (!mContributors.equals(that.mContributors)) return false;
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
        result = 31 * result + mContributors.hashCode();
        result = 31 * result + (mNote != null ? mNote.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Transaction another) {
        return 0;
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

    public enum TransactionType {
        Expense, Income
    }


}