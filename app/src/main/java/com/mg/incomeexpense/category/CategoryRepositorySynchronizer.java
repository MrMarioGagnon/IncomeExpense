package com.mg.incomeexpense.category;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.RepositorySynchronizerBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 2016-07-26.
 */
public class CategoryRepositorySynchronizer extends RepositorySynchronizerBase {

    private static final String LOG_TAG = CategoryRepositorySynchronizer.class.getSimpleName();

    public CategoryRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        super(contentResolver, itemUri, messages);
    }

    @Override
    public ObjectBase Save(@NonNull ObjectBase item) {
        super.Save(item);

        if (!(item instanceof Category)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Category");
        }

        if (!item.isDirty()) {
            return item;
        }

        final Category itemToBeSave = (Category) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.CategoryEntry.COLUMN_ID + "=?";
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
                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_EXTENSION_TYPE, itemToBeSave.getType().toString());
                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
                id = IncomeExpenseContract.CategoryEntry.getIdFromUri(newUri);
                rowsAffected = (id != null) ? 1 : 0;
                itemToBeSave.setId(id);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
            } else {
                // update item
                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_NAME, itemToBeSave.getName());
                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_EXTENSION_TYPE, itemToBeSave.getType().toString());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }
}
