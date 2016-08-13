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

    public static final String PATH_CATEGORY = "category";
    public static final String PATH_CONTRIBUTOR = "contributor";
    public static final String PATH_ACCOUNT = "account";
    public static final String PATH_PAYMENT_METHOD = "payment_method";
    public static final String PATH_TRANSACTION = "transaction";

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
        public static final String COLUMN_CONTRIBUTORS = "contributors";
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

    public static final class PaymentMethodEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PAYMENT_METHOD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENT_METHOD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PAYMENT_METHOD;

        public static final String TABLE_NAME = "payment_method";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_EXCHANGE_RATE = "exchangeRate";
        public static final String COLUMN_CONTRIBUTORS = "contributors";
        public static final String COLUMN_CLOSE = "close";

        public static Uri buildInstanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }


    }

    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        // Table name
        public static final String TABLE_NAME = "category";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SUB_CATEGORY = "subCategory";

        public static Uri buildInstanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

    public static final class TransactionEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSACTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRANSACTION;

        // Table name
        public static final String TABLE_NAME = "transaction_";

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_ACCOUNT_ID = "accountId";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_EXCHANGERATE = "exchangeRate";
        public static final String COLUMN_PAYMENTMETHOD_ID = "paymentMethodId";
        public static final String COLUMN_NOTE = "note";

        public static Uri buildInstanceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

    }

}
