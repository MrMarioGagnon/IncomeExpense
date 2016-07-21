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

    private static final int CONTRIBUTOR = 300;
    private static final int CONTRIBUTOR_WITH_ID = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final String sContributorIdSelection =
            IncomeExpenseContract.ContributorEntry.TABLE_NAME +
                    "." + IncomeExpenseContract.ContributorEntry.COLUMN_ID + " = ? ";

    private IncomeExpenseDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = IncomeExpenseContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, IncomeExpenseContract.PATH_CONTRIBUTOR, CONTRIBUTOR);
        matcher.addURI(authority, IncomeExpenseContract.PATH_CONTRIBUTOR + "/#", CONTRIBUTOR_WITH_ID);

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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
