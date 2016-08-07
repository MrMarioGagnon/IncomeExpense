package com.mg.incomeexpense.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by mario on 2016-07-19.
 */
public class IncomeExpenseProvider extends ContentProvider {

    private static final int ACCOUNT = 200;
    private static final int ACCOUNT_WITH_ID = 201;
    private static final int CONTRIBUTOR = 300;
    private static final int CONTRIBUTOR_WITH_ID = 301;
    private static final int PAYMENT_METHOD = 400;
    private static final int PAYMENT_METHOD_WITH_ID = 401;
    private static final int CATEGORY = 500;
    private static final int CATEGORY_WITH_ID = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final String sContributorIdSelection =
            IncomeExpenseContract.ContributorEntry.TABLE_NAME +
                    "." + IncomeExpenseContract.ContributorEntry.COLUMN_ID + " = ? ";
    private static final String sAccountIdSelection =
            IncomeExpenseContract.AccountEntry.TABLE_NAME +
                    "." + IncomeExpenseContract.AccountEntry.COLUMN_ID + " = ? ";
    private static final String sPaymentMethodIdSelection =
            IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME +
                    "." + IncomeExpenseContract.PaymentMethodEntry.COLUMN_ID + " = ? ";
    private static final String sCategoryIdSelection =
            IncomeExpenseContract.CategoryEntry.TABLE_NAME +
                    "." + IncomeExpenseContract.CategoryEntry.COLUMN_ID + " = ? ";

    private IncomeExpenseDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = IncomeExpenseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, IncomeExpenseContract.PATH_CONTRIBUTOR, CONTRIBUTOR);
        matcher.addURI(authority, IncomeExpenseContract.PATH_CONTRIBUTOR + "/#", CONTRIBUTOR_WITH_ID);
        matcher.addURI(authority, IncomeExpenseContract.PATH_ACCOUNT, ACCOUNT);
        matcher.addURI(authority, IncomeExpenseContract.PATH_ACCOUNT + "/#", ACCOUNT_WITH_ID);
        matcher.addURI(authority, IncomeExpenseContract.PATH_PAYMENT_METHOD, PAYMENT_METHOD);
        matcher.addURI(authority, IncomeExpenseContract.PATH_PAYMENT_METHOD + "/#", PAYMENT_METHOD_WITH_ID);
        matcher.addURI(authority, IncomeExpenseContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, IncomeExpenseContract.PATH_CATEGORY + "/#", CATEGORY_WITH_ID);

        return matcher;
    }

    private Cursor getContributorById(Uri uri, String[] projection) {

        long id = IncomeExpenseContract.ContributorEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sContributorIdSelection;
        selectionArgs = new String[]{String.valueOf(id)};

        return mOpenHelper.getReadableDatabase().query(IncomeExpenseContract.ContributorEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    private Cursor getAccountById(Uri uri, String[] projection) {

        long id = IncomeExpenseContract.AccountEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sAccountIdSelection;
        selectionArgs = new String[]{String.valueOf(id)};

        return mOpenHelper.getReadableDatabase().query(IncomeExpenseContract.AccountEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    private Cursor getPaymentMethodById(Uri uri, String[] projection) {

        long id = IncomeExpenseContract.PaymentMethodEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sPaymentMethodIdSelection;
        selectionArgs = new String[]{String.valueOf(id)};

        return mOpenHelper.getReadableDatabase().query(IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    private Cursor getCategoryById(Uri uri, String[] projection) {

        long id = IncomeExpenseContract.CategoryEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sCategoryIdSelection;
        selectionArgs = new String[]{String.valueOf(id)};

        return mOpenHelper.getReadableDatabase().query(IncomeExpenseContract.CategoryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new IncomeExpenseDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case CONTRIBUTOR:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IncomeExpenseContract.ContributorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTRIBUTOR_WITH_ID:
                retCursor = getContributorById(uri, projection);
                break;
            case ACCOUNT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IncomeExpenseContract.AccountEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ACCOUNT_WITH_ID:
                retCursor = getAccountById(uri, projection);
                break;
            case PAYMENT_METHOD:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PAYMENT_METHOD_WITH_ID:
                retCursor = getPaymentMethodById(uri, projection);
                break;
            case CATEGORY:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IncomeExpenseContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_WITH_ID:
                retCursor = getCategoryById(uri, projection);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CONTRIBUTOR:
                return IncomeExpenseContract.ContributorEntry.CONTENT_TYPE;
            case CONTRIBUTOR_WITH_ID:
                return IncomeExpenseContract.ContributorEntry.CONTENT_ITEM_TYPE;
            case ACCOUNT:
                return IncomeExpenseContract.AccountEntry.CONTENT_TYPE;
            case ACCOUNT_WITH_ID:
                return IncomeExpenseContract.AccountEntry.CONTENT_ITEM_TYPE;
            case PAYMENT_METHOD:
                return IncomeExpenseContract.PaymentMethodEntry.CONTENT_TYPE;
            case PAYMENT_METHOD_WITH_ID:
                return IncomeExpenseContract.PaymentMethodEntry.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return IncomeExpenseContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_WITH_ID:
                return IncomeExpenseContract.CategoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CONTRIBUTOR: {
                long _id = db.insert(IncomeExpenseContract.ContributorEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = IncomeExpenseContract.ContributorEntry.buildInstanceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ACCOUNT: {
                long _id = db.insert(IncomeExpenseContract.AccountEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = IncomeExpenseContract.AccountEntry.buildInstanceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PAYMENT_METHOD: {
                long _id = db.insert(IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = IncomeExpenseContract.PaymentMethodEntry.buildInstanceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CATEGORY: {
                long _id = db.insert(IncomeExpenseContract.CategoryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = IncomeExpenseContract.CategoryEntry.buildInstanceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case CONTRIBUTOR:
                rowsDeleted = db.delete(
                        IncomeExpenseContract.ContributorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ACCOUNT:
                rowsDeleted = db.delete(
                        IncomeExpenseContract.AccountEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PAYMENT_METHOD:
                rowsDeleted = db.delete(
                        IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = db.delete(
                        IncomeExpenseContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CONTRIBUTOR:
                rowsUpdated = db.update(IncomeExpenseContract.ContributorEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ACCOUNT:
                rowsUpdated = db.update(IncomeExpenseContract.AccountEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PAYMENT_METHOD:
                rowsUpdated = db.update(IncomeExpenseContract.PaymentMethodEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = db.update(IncomeExpenseContract.CategoryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
