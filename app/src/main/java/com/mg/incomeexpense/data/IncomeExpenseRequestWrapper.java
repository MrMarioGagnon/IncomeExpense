package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.AccountEntry.COLUMN_NAME}, selection, selectionArgument, null);
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

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.CategoryEntry.COLUMN_NAME}, selection, selectionArgument, null);
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

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.ContributorEntry.COLUMN_NAME}, selection, selectionArgument, null);
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

            cursor = contentResolver.query(uri, new String[]{IncomeExpenseContract.PaymentMethodEntry.COLUMN_NAME}, selection, selectionArgument, null);
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
            cursor = contentResolver.query(IncomeExpenseContract.ContributorEntry.CONTENT_URI, null, null, null, "name asc");
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

}