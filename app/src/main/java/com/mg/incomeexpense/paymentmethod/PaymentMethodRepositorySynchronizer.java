package com.mg.incomeexpense.paymentmethod;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.RepositorySynchronizerBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 3/14/2016.
 */
public class PaymentMethodRepositorySynchronizer extends RepositorySynchronizerBase {

    private static final String LOG_TAG = PaymentMethodRepositorySynchronizer.class.getSimpleName();

    public PaymentMethodRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        super(contentResolver, itemUri, messages);
    }

    public ObjectBase Save(@NonNull ObjectBase item) {
        super.Save(item);

        // region Precondition
        if (!(item instanceof PaymentMethod)) {
            throw new IllegalArgumentException("Parameter item must be an instance of PaymentMethod");
        }

        if (!item.isDirty()) {
            return item;
        }
        // endregion Precondition

        final PaymentMethod itemToBeSave = (PaymentMethod) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.PaymentMethodEntry.COLUMN_ID + "=?";
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
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_EXCHANGE_RATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_OWNER_ID, itemToBeSave.getOwner().getId());
                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
                id = IncomeExpenseContract.AccountEntry.getIdFromUri(newUri);
                rowsAffected = (id != null) ? 1 : 0;
                itemToBeSave.setId(id);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
            } else {
                // update item
                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_EXCHANGE_RATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.PaymentMethodEntry.COLUMN_OWNER_ID, itemToBeSave.getOwner().getId());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }}
