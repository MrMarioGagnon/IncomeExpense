package com.mg.incomeexpense.category;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.extension.ExtensionFragmentFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mario on 2016-11-13.
 */

public final class Category extends ObjectBase implements Serializable, Comparable<Category> {

    private String mName;
    private ExtensionFragmentFactory.ExtensionType mExtensionType;

    public static Category create(@NonNull Cursor cursor){

        Objects.requireNonNull(cursor, "Parameter cursor of type Cursor is mandatory.");

        Category newInstance = new Category();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_NAME));
        String extensionType = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_EXTENSION_TYPE));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mExtensionType = ExtensionFragmentFactory.ExtensionType.valueOf(extensionType);

        return newInstance;
    }

    public static Category createNew() {

        Category newInstance = new Category();
        newInstance.mNew = true;
        newInstance.mDirty = true;
        newInstance.mName = "";
        newInstance.mExtensionType = ExtensionFragmentFactory.ExtensionType.NOTE;

        return newInstance;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!mName.equals(category.mName)) return false;
        return mExtensionType == category.mExtensionType;

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mExtensionType.hashCode();
        return result;
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


    public ExtensionFragmentFactory.ExtensionType getType() {
        return mExtensionType;
    }

    public void setType(@NonNull ExtensionFragmentFactory.ExtensionType type) {
        Objects.requireNonNull(type, "Parameter type of type ExtensionType is mandatory");

        if (!mExtensionType.equals(type)) {
            mDirty = true;
            this.mExtensionType = type;
        }

    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int compareTo(@NonNull Category instanceToCompare) {

        Objects.requireNonNull(instanceToCompare, "Parameter instanceToCompare of type Category is mandatory");

        return getName().compareToIgnoreCase(instanceToCompare.getName());
    }
}
