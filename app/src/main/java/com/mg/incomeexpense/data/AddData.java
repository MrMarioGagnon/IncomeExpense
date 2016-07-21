package com.mg.incomeexpense.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * Created by mario on 2016-07-20.
 */
public class AddData {

    public static void addData(Context context) {

        Uri contributorUri = IncomeExpenseContract.ContributorEntry.CONTENT_URI;
        ContentValues cv = new ContentValues();
        ContentResolver cr = context.getContentResolver();
        cv.put("Name", "Nathalie");
        cr.insert(contributorUri,cv);
        cv.clear();

        cv.put("Name", "Mario");
        cr.insert(contributorUri,cv);
        cv.clear();

    }
}
