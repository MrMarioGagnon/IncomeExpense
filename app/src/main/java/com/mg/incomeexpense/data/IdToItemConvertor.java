package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ApplicationConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-25.
 */
public class IdToItemConvertor {

    public static Contributor ConvertIdToContributor(@NonNull ContentResolver contentResolver, @NonNull Long id) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(id, "Parameter id of type Long is mandatory");

        Uri uri = IncomeExpenseContract.ContributorEntry.buildInstanceUri(id);
        Contributor contributor = null;

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                contributor = Contributor.create(cursor);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return contributor;
    }


    public static List<Contributor> ConvertIdsToContributors(@NonNull ContentResolver contentResolver, @NonNull Uri uri, @NonNull String stringId, String separator) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(uri, "Parameter uri of type Uri is mandatory");
        Objects.requireNonNull(stringId, "Parameter stringId of type String is mandatory");

        // Parameter validation
        if (separator == null) {
            separator = ApplicationConstant.storageSeparator;
        }

        List<Contributor> items = new ArrayList<>();
        List ids = Arrays.asList(stringId.split(separator));

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);

            int itemsToFoundCount = ids.size();
            int itemFound = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_ID));

                if (ids.contains(id.toString())) {
                    String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));
                    items.add(Contributor.create(id, name));
                    itemFound++;
                    if (itemFound == itemsToFoundCount)
                        break;
                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        Collections.sort(items);
        return items;
    }

    public static List<Category> ConvertIdsToCategories(@NonNull ContentResolver contentResolver, @NonNull Uri uri, @NonNull String stringId, String separator) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(uri, "Parameter uri of type Uri is mandatory");
        Objects.requireNonNull(stringId, "Parameter stringId of type String is mandatory");

        // Parameter validation
        if (separator == null) {
            separator = ApplicationConstant.storageSeparator;
        }

        List<Category> items = new ArrayList<>();
        List ids = Arrays.asList(stringId.split(separator));

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);

            int itemsToFoundCount = ids.size();
            int itemFound = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_ID));

                if (ids.contains(id.toString())) {
                    items.add(Category.create(cursor));
                    itemFound++;
                    if (itemFound == itemsToFoundCount)
                        break;
                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        Collections.sort(items);
        return items;
    }


}
