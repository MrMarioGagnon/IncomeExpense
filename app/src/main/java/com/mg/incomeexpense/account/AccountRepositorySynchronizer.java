package com.mg.incomeexpense.account;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerAction;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerException;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Map;

/**
 * Created by mario on 2016-07-26.
 */
public class AccountRepositorySynchronizer {

    private static final String LOG_TAG = AccountRepositorySynchronizer.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private final Uri mItemUri;
    private final Map<Integer, String> mMessages;

    public AccountRepositorySynchronizer(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {
        mMessages = messages;
        mItemUri = itemUri;
        mContentResolver = contentResolver;
    }

    public ObjectBase Save(@NonNull ObjectBase item) throws ItemRepositorySynchronizerException {

        // region Precondition
        if (!(item instanceof Account)) {
            throw new IllegalArgumentException("Parameter item must be an instance of Account");
        }

        if ((!item.isNew()) && item.getId() == null) {
            throw new NullPointerException("Item is not new, item id is mandatory.");
        }

        if (!item.isDirty()) {
            return item;
        }
        // endregion Precondition

        final Account itemToBeSave = (Account) item;
        final String itemType = itemToBeSave.getClass().getSimpleName();
        Long id;

        final String selection = IncomeExpenseContract.AccountEntry.COLUMN_ID + "=?";
        final String[] selectionArgs;
        int rowsAffected;
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentProviderResult[] results;

        if (itemToBeSave.isDead()) {
            // Delete item
            id = itemToBeSave.getId();
            selectionArgs = new String[]{id.toString()};

            operations.add(
                    ContentProviderOperation.newDelete(IncomeExpenseContract.AccountContributorEntry.CONTENT_URI).withSelection(
                            IncomeExpenseContract.AccountContributorEntry.COLUMN_ACCOUNT_ID + "=?", new String[]{String.valueOf(id)}).build());

            operations.add(
                    ContentProviderOperation.newDelete(IncomeExpenseContract.AccountCategoryEntry.CONTENT_URI).withSelection(
                            IncomeExpenseContract.AccountCategoryEntry.COLUMN_ACCOUNT_ID + "=?", new String[]{String.valueOf(id)}).build());

            operations.add(
                    ContentProviderOperation.newDelete(mItemUri).withSelection(selection, selectionArgs).build()
            );

            Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleting_item), itemType, id));

            try {
                results = mContentResolver.applyBatch(
                        IncomeExpenseContract.CONTENT_AUTHORITY, operations);
                rowsAffected = results[0].count;
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_deleted_associated_contributor), rowsAffected));

                rowsAffected = results[1].count;
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_deleted_associated_category), rowsAffected));

                rowsAffected = results[2].count;
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_deleted_item), itemType, rowsAffected, id));
            } catch (Exception ex) {
                Log.e(LOG_TAG, mMessages.get(R.string.log_error_deleting_item), ex);
                throw new ItemRepositorySynchronizerException(ex, ItemRepositorySynchronizerAction.delete);
            }

        } else {
            if (itemToBeSave.isNew()) {
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_adding_item), itemType));

                ContentValues accountValues = new ContentValues();
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_NAME, itemToBeSave.getName());
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());

                operations.add(
                        ContentProviderOperation.newInsert(mItemUri)
                                .withValues(accountValues)
                                .build());

                for (Contributor contributor : itemToBeSave.getContributors()) {
                    accountValues = new ContentValues();
                    accountValues.put(IncomeExpenseContract.AccountContributorEntry.COLUMN_ACCOUNT_ID, 0);
                    accountValues.put(IncomeExpenseContract.AccountContributorEntry.COLUMN_CONTRIBUTOR_ID, contributor.getId());
                    operations.add(
                            ContentProviderOperation.newInsert(IncomeExpenseContract.AccountContributorEntry.CONTENT_URI)
                                    .withValues(accountValues)
                                    .withValueBackReference(IncomeExpenseContract.AccountContributorEntry.COLUMN_ACCOUNT_ID, 0)
                                    .build());

                }

                for (Category category : itemToBeSave.getCategories()) {
                    accountValues = new ContentValues();
                    accountValues.put(IncomeExpenseContract.AccountCategoryEntry.COLUMN_ACCOUNT_ID, 0);
                    accountValues.put(IncomeExpenseContract.AccountCategoryEntry.COLUMN_CATEGORY_ID, category.getId());
                    operations.add(
                            ContentProviderOperation.newInsert(IncomeExpenseContract.AccountCategoryEntry.CONTENT_URI)
                                    .withValues(accountValues)
                                    .withValueBackReference(IncomeExpenseContract.AccountCategoryEntry.COLUMN_ACCOUNT_ID, 0)
                                    .build());

                }


                try {

                    results = mContentResolver.applyBatch(
                            IncomeExpenseContract.CONTENT_AUTHORITY, operations);

                    id = IncomeExpenseContract.AccountEntry.getIdFromUri(results[0].uri);
                    rowsAffected = (id != null) ? 1 : 0;
                    itemToBeSave.setId(id);
                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_added_item), itemType, rowsAffected, id));

                    //TODO Ajouter nouvelle logique pour contributor et category
//                    rowsAffected = 0;
//                    for (int i = 1; i < results.length; i++) {
//                        id = IncomeExpenseContract.AccountContributorEntry.getIdFromUri(results[i].uri);
//                        rowsAffected += (id != null) ? 1 : 0;
//                    }
//
//                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_added_associated_contributor), rowsAffected));
//
//                    rowsAffected = 0;
//                    for (int i = 1; i < results.length; i++) {
//                        id = IncomeExpenseContract.AccountContributorEntry.getIdFromUri(results[i].uri);
//                        rowsAffected += (id != null) ? 1 : 0;
//                    }
//
//                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_added_associated_contributor), rowsAffected));


                } catch (Exception ex) {
                    Log.e(LOG_TAG, mMessages.get(R.string.log_error_adding_item), ex);
                    throw new ItemRepositorySynchronizerException(ex, ItemRepositorySynchronizerAction.add);
                }

            } else {
                // update item
                id = itemToBeSave.getId();
                selectionArgs = new String[]{id.toString()};
                Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updating_item), itemType, id));

                ContentValues accountValues = new ContentValues();
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_NAME, itemToBeSave.getName());
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CURRENCY, itemToBeSave.getCurrency());
                accountValues.put(IncomeExpenseContract.AccountEntry.COLUMN_CLOSE, itemToBeSave.getIsClose());

                operations.add(
                        ContentProviderOperation.newUpdate(mItemUri)
                                .withSelection(selection, selectionArgs)
                                .withValues(accountValues).build());

                operations.add(
                        ContentProviderOperation.newDelete(IncomeExpenseContract.AccountContributorEntry.CONTENT_URI)
                                .withSelection(IncomeExpenseContract.AccountContributorEntry.COLUMN_ACCOUNT_ID + "=?", new String[]{id.toString()})
                                .build());

                for (Contributor contributor : itemToBeSave.getContributors()) {
                    accountValues = new ContentValues();
                    accountValues.put(IncomeExpenseContract.AccountContributorEntry.COLUMN_ACCOUNT_ID, id);
                    accountValues.put(IncomeExpenseContract.AccountContributorEntry.COLUMN_CONTRIBUTOR_ID, contributor.getId());
                    operations.add(
                            ContentProviderOperation.newInsert(IncomeExpenseContract.AccountContributorEntry.CONTENT_URI)
                                    .withValues(accountValues)
                                    .build());
                }

                operations.add(
                        ContentProviderOperation.newDelete(IncomeExpenseContract.AccountCategoryEntry.CONTENT_URI)
                                .withSelection(IncomeExpenseContract.AccountCategoryEntry.COLUMN_ACCOUNT_ID + "=?", new String[]{id.toString()})
                                .build());

                for (Category category : itemToBeSave.getCategories()) {
                    accountValues = new ContentValues();
                    accountValues.put(IncomeExpenseContract.AccountCategoryEntry.COLUMN_ACCOUNT_ID, id);
                    accountValues.put(IncomeExpenseContract.AccountCategoryEntry.COLUMN_CATEGORY_ID, category.getId());
                    operations.add(
                            ContentProviderOperation.newInsert(IncomeExpenseContract.AccountCategoryEntry.CONTENT_URI)
                                    .withValues(accountValues)
                                    .build());
                }


                try {

                    results = mContentResolver.applyBatch(
                            IncomeExpenseContract.CONTENT_AUTHORITY, operations);

                    rowsAffected = results[0].count;
                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_updated_item), itemType, rowsAffected, id));

                    //todo add message for new logic
//                    rowsAffected = results[1].count;
//                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_deleted_associated_contributor), rowsAffected));
//
//                    rowsAffected = 0;
//                    for (int i = 2; i < results.length; i++) {
//                        id = IncomeExpenseContract.AccountContributorEntry.getIdFromUri(results[i].uri);
//                        rowsAffected += (id != null) ? 1 : 0;
//                    }
//
//                    Log.i(LOG_TAG, String.format(mMessages.get(R.string.log_info_number_added_associated_contributor), rowsAffected));
                } catch (Exception ex) {
                    Log.e(LOG_TAG, mMessages.get(R.string.log_error_updating_item), ex);
                    throw new ItemRepositorySynchronizerException(ex, ItemRepositorySynchronizerAction.update);
                }

            }
        }

        return itemToBeSave;
    }
}
