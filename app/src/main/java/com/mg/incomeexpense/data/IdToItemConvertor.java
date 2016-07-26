package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.mg.incomeexpense.contributor.Contributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mario on 2016-07-25.
 */
public class IdToItemConvertor {

    public static List<Contributor> ConvertIdsToContributors(ContentResolver contentResolver, Uri uri, String stringId, String separator) {

        // Parameter validation
        if (separator == null) {
            separator = ",";
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
        }finally{
            if(null!=cursor){
                cursor.close();
            }
        }

        Collections.sort(items);
        return items;
    }

}
