package com.mg.incomeexpense.contributor;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.Objects;

public class Contributor extends ObjectBase implements Serializable, Comparable<Contributor> {

    private String mName;

    private Contributor() {

    }

    public static Contributor create(@NonNull Cursor cursor) {

        Objects.requireNonNull(cursor, "Parameter cursor of type Cursor is mandatory.");

        Contributor newInstance = new Contributor();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));

        newInstance.mId = id;
        newInstance.mName = name;

        return newInstance;
    }


    public static Contributor create(@NonNull Long id, @NonNull String name) {

        Objects.requireNonNull(id, "Parameter id of type Long is mandatory");
        Objects.requireNonNull(name, "Parameter name of type String is mandatory");

        Contributor newInstance = new Contributor();
        newInstance.mNew = false;
        newInstance.mDirty = false;
        newInstance.mId = id;
        newInstance.mName = name;

        return newInstance;
    }

    public static Contributor createNew() {

        Contributor newInstance = new Contributor();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mName = "";

        return newInstance;

    }

    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {

        Objects.requireNonNull(name, "Parameter name of type String is mandatory");

        if (!mName.equals(name)) {
            mDirty = true;
            mName = name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(null == o) return false;
        if (this == o) return true;
        if (!(o instanceof Contributor)) return false;

        Contributor that = (Contributor) o;

        return mId.equals(that.mId) && mName.equals(that.mName);

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int compareTo(@NonNull Contributor instanceToCompare) {

        Objects.requireNonNull(instanceToCompare, "Parameter instanceToCompare of type Contributor is mandatory");

        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

}