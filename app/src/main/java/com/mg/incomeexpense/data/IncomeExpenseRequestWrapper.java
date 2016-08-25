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
import com.mg.incomeexpense.transaction.Transaction;
import com.mg.incomeexpense.transaction.TransactionAmountAccumulator;

import java.util.ArrayList;
import java.util.Calendar;
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

        String selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID);
        String[] selectionArgs = new String[]{account.getId().toString()};

        Date dFirstDateYear= DateUtil.getFirstDateOfYear(date).getTime();
        Date dLastDateYear = DateUtil.getLastDateOfYear(date).getTime();
        Date dFirstDateMonth = DateUtil.getFirstDateOfMonth(date).getTime();
        Date dLastDateMonth = DateUtil.getLastDateOfMonth(date).getTime();
        Date dFirstDateWeek = DateUtil.getFirstDateOfWeek(date).getTime();
        Date dLastDateWeek = DateUtil.getLastDateOfWeek(date).getTime();

        Integer firstDateYear = Integer.parseInt(Tools.formatDate(dFirstDateYear, "yyyyMMdd"));
        Integer lastDateYear = Integer.parseInt(Tools.formatDate(dLastDateYear, "yyyyMMdd"));
        Integer firstDateMonth = Integer.parseInt(Tools.formatDate(dFirstDateMonth, "yyyyMMdd"));
        Integer lastDateMonth = Integer.parseInt(Tools.formatDate(dLastDateMonth, "yyyyMMdd"));
        Integer firstDateWeek = Integer.parseInt(Tools.formatDate(dFirstDateWeek, "yyyyMMdd"));
        Integer lastDateWeek = Integer.parseInt(Tools.formatDate(dLastDateWeek, "yyyyMMdd"));
        Integer today = Integer.parseInt(Tools.formatDate(date, "yyyyMMdd"));

        String sToday = Tools.formatDate(date, "yyyy-MM-dd" );
        String sThisWeek = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateWeek, "yyyy-MM-dd" ), Tools.formatDate(dLastDateWeek, "yyyy-MM-dd" ));
        String sThisMonth = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateMonth, "yyyy-MM-dd" ), Tools.formatDate(dLastDateMonth, "yyyy-MM-dd" ));
        String sThisYear = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateYear, "yyyy-MM-dd" ), Tools.formatDate(dLastDateYear, "yyyy-MM-dd" ));;

        TransactionAmountAccumulator todayTotal = new TransactionAmountAccumulator(account.getContributors(), sToday);
        TransactionAmountAccumulator thisWeekTotal = new TransactionAmountAccumulator(account.getContributors(),sThisWeek);
        TransactionAmountAccumulator thisMonthTotal = new TransactionAmountAccumulator(account.getContributors(),sThisMonth);
        TransactionAmountAccumulator thisYearTotal = new TransactionAmountAccumulator(account.getContributors(),sThisYear);

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.TransactionEntry.CONTENT_URI, null, selection, selectionArgs, null);
            int transactionDate;
            Transaction transaction;
            Double amount;


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                transaction = Transaction.create(cursor, contentResolver);
                transactionDate = transaction.getDateAsInt();
                amount = transaction.getAmount() * transaction.getExchangeRate();

                if (transactionDate >= firstDateYear && transactionDate <= lastDateYear) {

                    thisYearTotal.Add(transaction.getPaymentMethod().getContributors(), transaction.getType(), amount);

                    if (transactionDate >= firstDateMonth && transactionDate <= lastDateMonth) {
                        thisMonthTotal.Add(transaction.getPaymentMethod().getContributors(), transaction.getType(), amount);
                    }

                    if (transactionDate >= firstDateWeek && transactionDate <= lastDateWeek) {
                        thisWeekTotal.Add(transaction.getPaymentMethod().getContributors(), transaction.getType(), amount);
                    }

                    if (transactionDate == today)
                        todayTotal.Add(transaction.getPaymentMethod().getContributors(), transaction.getType(), amount);

                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return new DashboardData(todayTotal.getTransactionAmountTotal(), thisWeekTotal.getTransactionAmountTotal(), thisMonthTotal.getTransactionAmountTotal(), thisYearTotal.getTransactionAmountTotal());
    }

}