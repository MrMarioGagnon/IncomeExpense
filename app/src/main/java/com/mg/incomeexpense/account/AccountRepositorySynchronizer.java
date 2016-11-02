package com.mg.incomeexpense.account;

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
 * Created by mario on 2016-07-26.
 */
public class AccountRepositorySynchronizer extends RepositorySynchronizerBase {

    private static final String LOG_TAG = AccountRepositorySynchronizer.class.getSimpleName();

    public AccountRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        super(contentResolver, itemUri, messages);
    }

    @Override
    public ObjectBase Save(@NonNull ObjectBase item) {
        super.Save(item);

        // region Precondition
        if (!(item instanceof Account)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Account");
        }

        final Account itemToBeSave = (Account) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.AccountEntry.COLUMN_ID + "=?";
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
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CATEGORIES, itemToBeSave.getCategoriesAsString());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_BUDGET, itemToBeSave.getBudget());
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
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CONTRIBUTORS, itemToBeSave.getContributorsIds());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CATEGORIES, itemToBeSave.getCategoriesAsString());
                itemValues.put(IncomeExpenseContract.AccountEntry.COLUMN_BUDGET, itemToBeSave.getBudget());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }
}
