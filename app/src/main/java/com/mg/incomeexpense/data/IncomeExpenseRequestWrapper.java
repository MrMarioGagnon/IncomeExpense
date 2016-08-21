package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.DateUtil;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;
import com.mg.incomeexpense.transaction.DashboardData;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mario on 2016-07-23.
 */
public class IncomeExpenseRequestWrapper {

    private static final String LOG_TAG = IncomeExpenseRequestWrapper.class.getSimpleName();

    public static ArrayList<String> getAvailableAccountName(@NonNull ContentResolver contentResolver, ObjectBase account) {

        ArrayList<String> names = new ArrayList<>();

        Uri uri = IncomeExpenseContract.AccountEntry.CONTENT_URI;

        Cursor cursor = null;
        try {

            String selection = String.format("%1$s !=?", IncomeExpenseContract.AccountEntry.COLUMN_ID);
            // Si account est new le id va etre null, donc remplacer par -1
            String[] selectionArgument = new String[]{account.isNew() ? "-1" : account.getId().toString()};

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.AccountEntry.COLUMN_NAME}, selection, selectionArgument, IncomeExpenseContract.AccountEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.AccountEntry.COLUMN_NAME));
                names.add(name.toUpperCase());
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailableCategoryName(@NonNull ContentResolver contentResolver, ObjectBase category) {

        ArrayList<String> names = new ArrayList<>();

        Uri uri = IncomeExpenseContract.CategoryEntry.CONTENT_URI;

        Cursor cursor = null;
        try {

            String selection = String.format("%1$s !=?", IncomeExpenseContract.CategoryEntry.COLUMN_ID);
            // Si category est new le id va etre null, donc remplacer par -1
            String[] selectionArgument = new String[]{category.isNew() ? "-1" : category.getId().toString()};

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.CategoryEntry.COLUMN_NAME}, selection, selectionArgument, IncomeExpenseContract.CategoryEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_NAME));
                names.add(name.toUpperCase());
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailableContributorName(ContentResolver contentResolver, Contributor contributor) {

        ArrayList<String> names = new ArrayList<>();

        Uri uri = IncomeExpenseContract.ContributorEntry.CONTENT_URI;

        Cursor cursor = null;
        try {

            String selection = String.format("%1$s !=?", IncomeExpenseContract.ContributorEntry.COLUMN_ID);
            // Si contributor est new le id va etre null, donc remplacer par -1
            String[] selectionArgument = new String[]{contributor.isNew() ? "-1" : contributor.getId().toString()};

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.ContributorEntry.COLUMN_NAME}, selection, selectionArgument, IncomeExpenseContract.ContributorEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));
                names.add(name.toUpperCase());
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailablePaymentMethodName(ContentResolver contentResolver, PaymentMethod paymentMethod) {

        ArrayList<String> names = new ArrayList<>();

        Uri uri = IncomeExpenseContract.PaymentMethodEntry.CONTENT_URI;

        Cursor cursor = null;
        try {

            String selection = String.format("%1$s !=?", IncomeExpenseContract.PaymentMethodEntry.COLUMN_ID);
            // Si contributor est new le id va etre null, donc remplacer par -1
            String[] selectionArgument = new String[]{paymentMethod.isNew() ? "-1" : paymentMethod.getId().toString()};

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME}, selection, selectionArgument, IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME));
                names.add(name.toUpperCase());
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<Contributor> getAvailableContributors(ContentResolver contentResolver) {

        ArrayList<Contributor> contributors = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.ContributorEntry.CONTENT_URI, null, null, null, IncomeExpenseContract.ContributorEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));
                contributors.add(Contributor.create(id, name));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return contributors;
    }

    public static ArrayList<Account> getAvailableAccounts(ContentResolver contentResolver) {

        ArrayList<Account> assets = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.AccountEntry.CONTENT_URI, null, null, null, IncomeExpenseContract.AccountEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                assets.add(Account.create(cursor, contentResolver));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return assets;
    }

    public static ArrayList<Category> getAvailableCategories(ContentResolver contentResolver) {

        ArrayList<Category> assets = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.CategoryEntry.CONTENT_URI, null, null, null, IncomeExpenseContract.CategoryEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                assets.add(Category.create(cursor, contentResolver));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return assets;
    }

    public static ArrayList<PaymentMethod> getAvailablePaymentMethods(ContentResolver contentResolver) {

        ArrayList<PaymentMethod> assets = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.PaymentMethodEntry.CONTENT_URI, null, null, null, IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                assets.add(PaymentMethod.create(cursor, contentResolver));
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return assets;
    }

    public static DashboardData getDashboardData(ContentResolver contentResolver, Account account, Date date) {

        String[] projection = new String[]{IncomeExpenseContract.TransactionEntry.COLUMN_TYPE,
                IncomeExpenseContract.TransactionEntry.COLUMN_DATE,
                IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT,
                IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE};

        String selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID);
        String[] selectionArgs = new String[]{account.getId().toString()};

        DashboardData dashboardData = new DashboardData();

        Integer sFirstDateYear = Integer.parseInt(Tools.formatDate(DateUtil.getFirstDateOfYear(date).getTime(), "yyyyMMdd"));
        Integer sLastDateYear = Integer.parseInt(Tools.formatDate(DateUtil.getLastDateOfYear(date).getTime(), "yyyyMMdd"));
        Integer sFirstDateMonth = Integer.parseInt(Tools.formatDate(DateUtil.getFirstDateOfMonth(date).getTime(), "yyyyMMdd"));
        Integer sLastDateMonth = Integer.parseInt(Tools.formatDate(DateUtil.getLastDateOfMonth(date).getTime(), "yyyyMMdd"));
        Integer sFirstDateWeek = Integer.parseInt(Tools.formatDate(DateUtil.getFirstDateOfWeek(date).getTime(), "yyyyMMdd"));
        Integer sLastDateWeek = Integer.parseInt(Tools.formatDate(DateUtil.getLastDateOfWeek(date).getTime(), "yyyyMMdd"));
        Integer sToday = Integer.parseInt(Tools.formatDate(date, "yyyyMMdd"));

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.TransactionEntry.CONTENT_URI, projection, selection, selectionArgs, null);
            Integer cursorDate;
            Double cursorAmount;
            Double cursorExchangeRate;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                cursorDate = Integer.parseInt(cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_DATE)).replace("-", ""));
                cursorAmount = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT));
                cursorExchangeRate = cursor.getDouble(cursor.getColumnIndex(IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE));

                if (cursorDate >= sFirstDateYear && cursorDate <= sLastDateYear) {

                    dashboardData.thisYear += cursorAmount * cursorExchangeRate;

                    if (cursorDate >= sFirstDateMonth && cursorDate <= sLastDateMonth) {
                        dashboardData.thisMonth += cursorAmount * cursorExchangeRate;
                    }

                    if (cursorDate >= sFirstDateWeek && cursorDate <= sLastDateWeek) {
                        dashboardData.thisWeek += cursorAmount * cursorExchangeRate;
                    }

                    if (cursorDate == sToday)
                        dashboardData.today += cursorAmount * cursorExchangeRate;
                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return dashboardData;
    }

}