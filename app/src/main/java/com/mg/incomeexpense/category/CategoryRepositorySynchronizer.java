package com.mg.incomeexpense.category;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 2016-07-26.
 */
public class CategoryRepositorySynchronizer {

    private static final String LOG_TAG = CategoryRepositorySynchronizer.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private final Uri mItemUri;
    private final Map<Integer, String> mMessages;

    public CategoryRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        mMessages = messages;
        mItemUri = itemUri;
        mContentResolver = contentResolver;
    }

    public ObjectBase Save(@NonNull ObjectBase item) {

        // region Precondition
        if (!(item instanceof Category)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Category");
        }

        if ((!item.isNew()) && item.getId() == null) {
            throw new NullPointerException("Item is not new, item id is mandatory.");
        }

        if (!item.isDirty()) {
            return item;
        }
        // endregion Precondition

        final Category itemToBeSave = (Category) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

//        final String selection = IncomeExpenseContract.CategoryEntry.COLUMN_ID + "=?";
//        final String[] selectionArgs;
//        int rowsAffected;
//
//        if (itemToBeSave.isDead()) {
//            // Delete item
//            id = itemToBeSave.getId();
//            selectionArgs = new String[]{id.toString()};
//            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleting_item), itemType, id));
//            rowsAffected = mContentResolver.delete(mItemUri, selection, selectionArgs);
//            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleted_item), itemType, rowsAffected, id));
//        } else {
//            if (itemToBeSave.isNew()) {
//                // add item
//                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_adding_item), itemType));
//                ContentValues itemValues = new ContentValues();
//                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_NAME, itemToBeSave.getName());
//                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_SUB_CATEGORY, itemToBeSave.getSubCategoriesAsString());
//                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
//                id = IncomeExpenseContract.AccountEntry.getIdFromUri(newUri);
//                rowsAffected = (id != null) ? 1 : 0;
//                itemToBeSave.setId(id);
//                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
//            } else {
//                // update item
//                id = itemToBeSave.getId();
//                selectionArgs = new String[]{id.toString()};
//                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
//                ContentValues itemValues = new ContentValues();
//                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_NAME, itemToBeSave.getName());
//                itemValues.put(IncomeExpenseContract.CategoryEntry.COLUMN_SUB_CATEGORY, itemToBeSave.getSubCategoriesAsString());
//                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
//                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
//            }
        //}

        return itemToBeSave;
    }
}
