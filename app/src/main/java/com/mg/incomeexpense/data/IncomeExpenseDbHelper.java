package com.mg.incomeexpense.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mario on 2016-07-19.
 */
public class IncomeExpenseDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = IncomeExpenseDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "incexp.db";
    private static final int DATABASE_VERSION = 1;

    public IncomeExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CONTRIBUTOR_TABLE = "CREATE TABLE " + IncomeExpenseContract.ContributorEntry.TABLE_NAME + " (" +
                IncomeExpenseContract.ContributorEntry._ID + " INTEGER PRIMARY KEY," +
                IncomeExpenseContract.ContributorEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL" +
                " );";

        db.execSQL(SQL_CREATE_CONTRIBUTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, String.format("Upgrading database from version %1$d to %2$d, which will destroy all old data.", oldVersion, newVersion) );
        db.execSQL(String.format("DROP TABLE IF EXISTS %1$s", IncomeExpenseContract.ContributorEntry.TABLE_NAME));
    }
}
