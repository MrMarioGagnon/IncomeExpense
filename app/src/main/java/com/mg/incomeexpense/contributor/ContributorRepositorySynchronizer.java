package com.mg.incomeexpense.contributor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 2016-07-23.
 */
public class ContributorRepositorySynchronizer {

    private static final String LOG_TAG = ContributorRepositorySynchronizer.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private final Uri mItemUri;
    private final Map<Integer, String> mMessages;

    public ContributorRepositorySynchronizer(ContentResolver contentResolver, Uri itemUri, Map<Integer, String> messages) {
        mMessages = messages;
        mItemUri = itemUri;
        mContentResolver = contentResolver;
    }

    public ObjectBase Save(ObjectBase item) {

        // region Precondition
        if (!(item instanceof Contributor)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Contributor");
        }

        if ((!item.isNew()) && item.getId() == null) {
            throw new NullPointerException("Item is not new, item id is mandatory.");
        }

        if (!item.isDirty()) {
            return item;
        }
        // endregion Precondition

        final Contributor itemToBeSave = (Contributor) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.ContributorEntry.COLUMN_ID + "=?";
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
                itemValues.put(IncomeExpenseContract.ContributorEntry.COLUMN_NAME, itemToBeSave.getName());
                Uri newUri = mContentResolver.insert(mItemUri, itemValues);
                id = IncomeExpenseContract.ContributorEntry.getIdFromUri(newUri);
                rowsAffected = (id != null) ? 1 : 0;
                itemToBeSave.setId(id);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));
            } else {
                // update item
                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));
                ContentValues itemValues = new ContentValues();
                itemValues.put(IncomeExpenseContract.ContributorEntry.COLUMN_NAME, itemToBeSave.getName());
                rowsAffected = mContentResolver.update(mItemUri, itemValues, selection, selectionArgs);
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));
            }
        }

        return itemToBeSave;
    }
}