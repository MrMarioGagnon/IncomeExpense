package com.mg.incomeexpense.transaction;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.Photo.Photo;
import com.mg.incomeexpense.Photo.PhotoManager;
import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.RepositorySynchronizerBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 3/14/2016.
 */
public class TransactionRepositorySynchronizer extends RepositorySynchronizerBase {

    private static final String LOG_TAG = TransactionRepositorySynchronizer.class.getSimpleName();

    public TransactionRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        super(contentResolver, itemUri, messages);
    }

    public ObjectBase Save(@NonNull ObjectBase item) {
        super.Save(item);

        if (!(item instanceof Transaction)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Transaction");
        }

        if (!item.isDirty()) {
            return item;
        }

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
            if(itemToBeSave.getPhoto() != null)
                PhotoManager.delete(itemToBeSave.getPhoto());
            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleted_item), itemType, rowsAffected, id));
        } else {
            if (itemToBeSave.isNew()) {
                // add item

                //TODO Save photo to storage
//                Photo photo = itemToBeSave.getPhoto();
//                if(photo.getPath() == null){
//                    PhotoManager.create()
//                }

                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_adding_item), itemType));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID, itemToBeSave.getAccount().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY_ID, itemToBeSave.getCategory().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_TYPE, itemToBeSave.getType().ordinal());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_DATE, itemToBeSave.getDate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT, itemToBeSave.getAmount());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID, itemToBeSave.getPaymentMethod().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_NOTE, itemToBeSave.getNote());
                //itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PHOTO_PATH, photo.getPath());
                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
                id = IncomeExpenseContract.TransactionEntry.getIdFromUri(newUri);
                rowsAffected = (id != null) ? 1 : 0;
                itemToBeSave.setId(id);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
            } else {
                // update item

                // TODO Save photo to storage
                Photo photo = itemToBeSave.getPhoto();

                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID, itemToBeSave.getAccount().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY_ID, itemToBeSave.getCategory().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_TYPE, itemToBeSave.getType().ordinal());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_DATE, itemToBeSave.getDate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT, itemToBeSave.getAmount());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE, itemToBeSave.getExchangeRate());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID, itemToBeSave.getPaymentMethod().getId());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_NOTE, itemToBeSave.getNote());
                //itemValues.put(IncomeExpenseContract.TransactionEntry.COLUMN_PHOTO_PATH, photo.getPath());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }
}
