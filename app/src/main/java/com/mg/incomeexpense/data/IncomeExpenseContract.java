package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mario on 2016-07-19.
 */
public class IncomeExpenseContract {

    public static final String CONTENT_AUTHORITY = "com.mg.incomeexpense";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTRIBUTOR = "contributor";
    public static final String PATH_ACCOUNT = "account";

    public static final class AccountEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNT;

        public static final String TABLE_NAME = "account";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_CLOSE = "close";

        public static Uri buildInstanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class ContributorEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTRIBUTOR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTRIBUTOR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTRIBUTOR;

        public static final String TABLE_NAME = "contributor";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME = "name";

        public static Uri buildInstanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }
}
