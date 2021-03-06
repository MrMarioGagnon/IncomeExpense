package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ApplicationConstant;
import com.mg.incomeexpense.core.DateUtil;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.dashboard.DashboardAmountAccumulator;
import com.mg.incomeexpense.dashboard.DashboardPeriodAmount;
import com.mg.incomeexpense.dashboard.DashboardPeriodTotal;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;
import com.mg.incomeexpense.transaction.Transaction;
import com.mg.incomeexpense.utility.UtilityActivity;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class IncomeExpenseRequestWrapper {

    private static final String LOG_TAG = IncomeExpenseRequestWrapper.class.getSimpleName();

    public static ArrayList<Transaction> getAllTransactionForAccount(@NonNull Context context, @NonNull Account account) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(account, "Parameter account of type Account is mandatory");

        ContentResolver contentResolver = context.getContentResolver();

        ArrayList<Transaction> transactions = new ArrayList<>();

        if (!account.isNew()) {

            Uri uri = IncomeExpenseContract.TransactionEntry.CONTENT_URI;

            Cursor cursor = null;
            try {

                String selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID);
                String[] selectionArgument = new String[]{account.getId().toString()};

                cursor = contentResolver.query(uri, null, selection, selectionArgument, null);
                Transaction transaction;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    transaction = Transaction.create(cursor, context);
                    transactions.add(transaction);
                }
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }

        return transactions;
    }

    public static ArrayList<Transaction> getAllTransactionForPaymentMethod(@NonNull Context context, @NonNull PaymentMethod paymentMethod) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(paymentMethod, "Parameter paymentMethod of type PaymentMethod is mandatory");

        ArrayList<Transaction> transactions = new ArrayList<>();

        if (!paymentMethod.isNew()) {
            Uri uri = IncomeExpenseContract.TransactionEntry.CONTENT_URI;

            Cursor cursor = null;
            try {

                String selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID);
                String[] selectionArgument = new String[]{paymentMethod.getId().toString()};

                cursor = context.getContentResolver().query(uri, null, selection, selectionArgument, null);
                Transaction transaction;
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    transaction = Transaction.create(cursor, context);
                    transactions.add(transaction);
                }
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }

        return transactions;
    }


    public static ArrayList<String> getAvailableAccountName(@NonNull ContentResolver contentResolver, @NonNull Account account) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(account, "Parameter account of type Account is mandatory");

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
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailableContributorName(@NonNull ContentResolver contentResolver, @NonNull Contributor contributor) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(contributor, "Parameter contributor of type Contributor is mandatory");

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
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailablePaymentMethodName(@NonNull ContentResolver contentResolver, @NonNull PaymentMethod paymentMethod) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(paymentMethod, "Parameter paymentMethod of type PaymentMethod is mandatory");

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
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<String> getAvailableCategoryName(@NonNull ContentResolver contentResolver, @NonNull Category category) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(category, "Parameter category of type Account is mandatory");

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
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return names;
    }

    public static ArrayList<Contributor> getAvailableContributors(@NonNull ContentResolver contentResolver) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");

        ArrayList<Contributor> contributors = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.ContributorEntry.CONTENT_URI, null, null, null, String.format("LOWER(%1$s)", IncomeExpenseContract.ContributorEntry.COLUMN_NAME));
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                contributors.add(Contributor.create(cursor));
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return contributors;
    }

    public static ArrayList<Category> getAvailableCategories(@NonNull ContentResolver contentResolver) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");

        ArrayList<Category> categories = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.CategoryEntry.CONTENT_URI, null, null, null, String.format("LOWER(%1$s)", IncomeExpenseContract.CategoryEntry.COLUMN_NAME));
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                categories.add(Category.create(cursor));
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return categories;
    }


    public static ArrayList<Account> getAvailableAccounts(@NonNull ContentResolver contentResolver, String order) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");

        ArrayList<Account> assets = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.AccountEntry.CONTENT_URI, null, null, null, order);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                assets.add(Account.create(cursor, contentResolver));
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return assets;
    }

    public static ArrayList<PaymentMethod> getAvailablePaymentMethods(@NonNull ContentResolver contentResolver, List<Contributor> contributors) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");

        ArrayList<PaymentMethod> assets = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(IncomeExpenseContract.PaymentMethodEntry.CONTENT_URI, null, null, null, String.format("LOWER(%1$s)", IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME));
            PaymentMethod paymentMethod = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                paymentMethod = PaymentMethod.create(cursor, contentResolver);

                if (null != contributors && !contributors.contains(paymentMethod.getOwner())) {
                    continue;
                }
                assets.add(paymentMethod);

            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return assets;
    }

    public static DashboardPeriodTotal getDashboardData(@NonNull Context context, @NonNull Account account, @NonNull LocalDate date) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(account, "Parameter account of type Account is mandatory");
        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Boolean t;

        String selection = null;
        String[] selectionArgs = null;
        if (account.getId() != 0) {
            selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID);
            selectionArgs = new String[]{account.getId().toString()};
        }

        LocalDate dFirstDateYear = DateUtil.getFirstDateOfYear(date);
        LocalDate dLastDateYear = DateUtil.getLastDateOfYear(date);
        LocalDate dFirstDateMonth = DateUtil.getFirstDateOfMonth(date);
        LocalDate dLastDateMonth = DateUtil.getLastDateOfMonth(date);
        LocalDate dFirstDateWeek = DateUtil.getFirstDateOfWeek(date);
        LocalDate dLastDateWeek = DateUtil.getLastDateOfWeek(date);

        LocalDate dFirstDateLastYear = DateUtil.getFirstDateOfYear(date.minusYears(1));
        LocalDate dLastDateLastYear = DateUtil.getLastDateOfYear(date.minusYears(1));

        Integer firstDateYear = Integer.parseInt(Tools.formatDate(dFirstDateYear, ApplicationConstant.dateFormat2));
        Integer lastDateYear = Integer.parseInt(Tools.formatDate(dLastDateYear, ApplicationConstant.dateFormat2));
        Integer firstDateMonth = Integer.parseInt(Tools.formatDate(dFirstDateMonth, ApplicationConstant.dateFormat2));
        Integer lastDateMonth = Integer.parseInt(Tools.formatDate(dLastDateMonth, ApplicationConstant.dateFormat2));
        Integer firstDateWeek = Integer.parseInt(Tools.formatDate(dFirstDateWeek, ApplicationConstant.dateFormat2));
        Integer lastDateWeek = Integer.parseInt(Tools.formatDate(dLastDateWeek, ApplicationConstant.dateFormat2));
        Integer today = Integer.parseInt(Tools.formatDate(date, ApplicationConstant.dateFormat2));
        Integer firstDateLastYear = Integer.parseInt(Tools.formatDate(dFirstDateLastYear, ApplicationConstant.dateFormat2));
        Integer lastDateLastYear = Integer.parseInt(Tools.formatDate(dLastDateLastYear, ApplicationConstant.dateFormat2));

        String sToday = Tools.formatDate(date, ApplicationConstant.dateFormat1);
        String sThisWeek = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateWeek, ApplicationConstant.dateFormat1), Tools.formatDate(dLastDateWeek, ApplicationConstant.dateFormat1));
        String sThisMonth = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateMonth, ApplicationConstant.dateFormat1), Tools.formatDate(dLastDateMonth, ApplicationConstant.dateFormat1));
        String sThisYear = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateYear, ApplicationConstant.dateFormat1), Tools.formatDate(dLastDateYear, ApplicationConstant.dateFormat1));
        String sLastYear = String.format("%1$s - %2$s", Tools.formatDate(dFirstDateLastYear, ApplicationConstant.dateFormat1), Tools.formatDate(dLastDateLastYear, ApplicationConstant.dateFormat1));

        DashboardAmountAccumulator todayTotal = new DashboardAmountAccumulator(account.getContributors(), String.format("%1$s : %2$s", context.getString(R.string.title_today), sToday), DashboardPeriodAmount.Type.Today);
        DashboardAmountAccumulator thisWeekTotal = new DashboardAmountAccumulator(account.getContributors(), String.format("%1$s : %2$s", context.getString(R.string.title_this_week), sThisWeek), DashboardPeriodAmount.Type.Week);
        DashboardAmountAccumulator thisMonthTotal = new DashboardAmountAccumulator(account.getContributors(), String.format("%1$s : %2$s", context.getString(R.string.title_this_month), sThisMonth), DashboardPeriodAmount.Type.Month);
        DashboardAmountAccumulator thisYearTotal = new DashboardAmountAccumulator(account.getContributors(), String.format("%1$s : %2$s", context.getString(R.string.title_this_year), sThisYear), DashboardPeriodAmount.Type.Year);
        DashboardAmountAccumulator lastYearTotal = new DashboardAmountAccumulator(account.getContributors(), String.format("%1$s : %2$s", context.getString(R.string.title_last_year), sLastYear), DashboardPeriodAmount.Type.LastYear);

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(IncomeExpenseContract.TransactionEntry.CONTENT_URI, null, selection, selectionArgs, null);

            int transactionDate;
            Transaction transaction;
            Double amount;

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                transaction = Transaction.create(cursor, context);
                transactionDate = transaction.getDateAsInt();
                amount = transaction.getAmount() * transaction.getExchangeRate();

                if (transactionDate >= firstDateYear && transactionDate <= lastDateYear) {

                    thisYearTotal.Add(transaction.getContributors(), transaction.getType(), amount);

                    if (transactionDate >= firstDateMonth && transactionDate <= lastDateMonth) {
                        thisMonthTotal.Add(transaction.getContributors(), transaction.getType(), amount);
                    }

                    if (transactionDate >= firstDateWeek && transactionDate <= lastDateWeek) {
                        thisWeekTotal.Add(transaction.getContributors(), transaction.getType(), amount);
                    }

                    if (transactionDate == today)
                        todayTotal.Add(transaction.getContributors(), transaction.getType(), amount);

                }
                if (transactionDate >= firstDateLastYear && transactionDate <= lastDateLastYear) {

                    lastYearTotal.Add(transaction.getContributors(), transaction.getType(), amount);

                }

            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return new DashboardPeriodTotal(todayTotal.getPeriodAmountTotal(), thisWeekTotal.getPeriodAmountTotal(), thisMonthTotal.getPeriodAmountTotal(), thisYearTotal.getPeriodAmountTotal(), lastYearTotal.getPeriodAmountTotal());
    }

}