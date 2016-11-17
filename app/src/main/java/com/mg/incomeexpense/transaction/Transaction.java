package com.mg.incomeexpense.transaction;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IdToItemConvertor;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class Transaction extends ObjectBase implements Serializable, Comparable<Transaction> {

    private Account mAccount;
    private Category mCategory;
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

    public static Transaction create(@NonNull Cursor cursor, @NonNull ContentResolver contentResolver) {

        Objects.requireNonNull(cursor, "Parameter cursor of type Cursor is mandatory.");
        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory.");

        Transaction newInstance = new Transaction();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_ID));
        Long accountId = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID));
        Long categoryId = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY_ID));
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

        subItemCursor = contentResolver.query(IncomeExpenseContract.CategoryEntry.buildInstanceUri(categoryId), null, null, null, null);
        Category category = null;
        if (subItemCursor != null) {
            if (subItemCursor.moveToFirst()) {
                category = Category.create(subItemCursor);
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

    public void setContributors(@NonNull List<Contributor> contributors) {

        Objects.requireNonNull(contributors, "Parameter contributors of type List<Contributor> is mandatory");

        if (!mContributors.equals(contributors)) {
            mDirty = true;
            this.mContributors = contributors;
        }
    }


    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(@NonNull Account account) {

        Objects.requireNonNull(account, "Parameter account of type Account is mandatory");

        if (mAccount == null || !mAccount.equals(account)) {
            mDirty = true;
            mAccount = account;
        }
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(@NonNull Double amount) {

        Objects.requireNonNull(amount, "Parameter amount of type Double is mandatory");

        if (mAmount == null || !mAmount.equals(amount)) {
            mDirty = true;
            mAmount = amount;
        }
    }

    public String getAmountAsString() {
        return Tools.formatAmount(getAmount());
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(@NonNull Category category) {

        Objects.requireNonNull(category, "Parameter category of type Category is mandatory");

        if (mCategory == null || !mCategory.equals(category)) {
            mDirty = true;
            mCategory = category;
        }
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(@NonNull String currency) {

        Objects.requireNonNull(currency, "Parameter currency of type String is mandatory");

        if (!mCurrency.equals(currency)) {
            mDirty = true;
            mCurrency = currency;
        }
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(@NonNull String date) {

        Objects.requireNonNull(date, "Parameter date of type String is mandatory");

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

    public void setExchangeRate(@NonNull Double exchangeRate) {

        Objects.requireNonNull(exchangeRate, "Parameter exchangeRate of type Double is mandatory");

        if (mExchangeRate == null || !mExchangeRate.equals(exchangeRate)) {
            mDirty = true;
            mExchangeRate = exchangeRate;
        }
    }

    public String getExchangeRateAsString() {
        return Tools.formatAmount(getExchangeRate());
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(@NonNull String note) {

        Objects.requireNonNull(note, "Parameter note of type String is mandatory");

        if (!mNote.equals(note)) {
            mDirty = true;
            mNote = note;
        }
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(@NonNull PaymentMethod paymentMethod) {

        Objects.requireNonNull(paymentMethod, "Parameter paymentMethod of type PaymentMethod is mandatory");

        if (mPaymentMethod == null || !mPaymentMethod.equals(paymentMethod)) {
            mDirty = true;
            mPaymentMethod = paymentMethod;
        }
    }

    public TransactionType getType() {
        return mType;
    }

    public void setType(@NonNull TransactionType type) {

        Objects.requireNonNull(type, "Parameter type of type TransactionType is mandatory");

        if (!mType.equals(type)) {
            mDirty = true;
            mType = type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) return false;
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
        return Tools.join(mContributors, ApplicationConstant.displaySeparator);
    }

    public String getContributorsIds() {
        List<String> a = new ArrayList<>();
        for (Contributor item : mContributors) {
            a.add(item.getId().toString());
        }
        return Tools.join(a, ApplicationConstant.storageSeparator);
    }

    public enum TransactionType {
        Expense, Income
    }

}