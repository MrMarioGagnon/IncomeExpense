package com.mg.incomeexpense.contributor;

import android.database.Cursor;

import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;

public class Contributor extends ObjectBase implements Serializable, Comparable<Contributor> {

    private String mName;

    private Contributor() {

    }

    public static Contributor create(Cursor cursor) {
        Contributor newInstance = new Contributor();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));

        newInstance.mId = id;
        newInstance.mName = name;

        return newInstance;
    }


    public static Contributor create(Long id, String name) {

        if (null == id)
            throw new NullPointerException("id parameter is mandatory");

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
        return null == mName ? "" : mName;
    }

    public void setName(String name) {

        if (!mName.equals(name)) {
            mDirty = true;
            mName = name;
        }
    }

    @Override
    public boolean equals(Object o) {
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
    public int compareTo(Contributor instanceToCompare) {
        if (null == instanceToCompare)
            throw new NullPointerException("Parameter instanceToCompare of type Contributor is mandatory");

        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }

}