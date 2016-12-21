package com.mg.incomeexpense.data;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mario on 2016-11-23.
 */

public class DatabaseMigration {
    public final static void moveTo2(SQLiteDatabase db) {

        db.execSQL(String.format("alter table %1$s add %2$s INTERGER NOT NULL DEFAULT 9999", IncomeExpenseContract.AccountEntry.TABLE_NAME, IncomeExpenseContract.AccountEntry.COLUMN_POSITION));

    }

    public final static void moveTo3(SQLiteDatabase db) {

        db.execSQL(String.format("alter table %1$s add %2$s TEXT NULL", IncomeExpenseContract.TransactionEntry.TABLE_NAME, IncomeExpenseContract.TransactionEntry.COLUMN_PHOTO_PATH));

    }

}
