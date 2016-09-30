package com.mg.incomeexpense.transaction;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.Map;

/**
 * Created by mario on 3/14/2016.
 */
public class TransactionRepositorySynchronizer {

    private static final String LOG_TAG = TransactionRepositorySynchronizer.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private final Uri mItemUri;
    private final Map<Integer, String> mMessages;

    public TransactionRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        mMessages = messages;
        mItemUri = itemUri;
        mContentResolver = contentResolver;
    }

    public ObjectBase Save(@NonNull ObjectBase item) {

        // region Precondition
        if (!(item instanceof Transaction)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Transaction");
        }

        if ((!item.isNew()) && item.getId() == null) {
            throw new NullPointerException("Item is not new, item id is mandatory.");
        }

        if (!item.isDirty()) {
            return item;
        }
        // endregion Precondition

        final Transaction itemToBeSave = (Transaction) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.TransactionEntry.COLUMN_ID + "=?";
        final String[] selectionArgs;
        int rowsAffected;

        if (itemToBeSave.isDead()) {
            // Delete item
            id = itemToBeSave.getId();
            selectionArgs = new String[]{id.toString()};
            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleting_item), itemType, id));
            rowsAffected = mContentResolver.delete(mItemUri, selection, selectionArgs);
            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleted_item), itemType, rowsAffected, id));
        } else {
            if (itemToBeSave.isNew()) {
                // add item
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_adding_item), itemType));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID, itemToBeSave.getAccount().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY, itemToBeSave.getCategory().getSelectedCategory());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_TYPE, itemToBeSave.getType().ordinal());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_DATE, itemToBeSave.getDate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT, itemToBeSave.getAmount());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID, itemToBeSave.getPaymentMethod().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_NOTE, itemToBeSave.getNote());
                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
                id = IncomeExpenseContract.TransactionEntry.getIdFromUri(newUri);
                rowsAffected = (id != null) ? 1 : 0;
                itemToBeSave.setId(id);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
            } else {
                // update item
                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID, itemToBeSave.getAccount().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY, itemToBeSave.getCategory().getSelectedCategory());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_TYPE, itemToBeSave.getType().ordinal());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_DATE, itemToBeSave.getDate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT, itemToBeSave.getAmount());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID, itemToBeSave.getPaymentMethod().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_NOTE, itemToBeSave.getNote());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }}
