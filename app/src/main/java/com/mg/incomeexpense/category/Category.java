package com.mg.incomeexpense.category;

import android.content.ContentResolver;
import android.database.Cursor;

import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by mario on 2016-08-05.
 */
public class Category extends ObjectBase implements Serializable {

    public static Category create(Cursor cursor) {
        Category newInstance = new Category();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_NAME));
        String subCategories = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_SUB_CATEGORY));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mSubCategories = subCategories.split("\\|");

        return newInstance;
    }

    public static Category create(Long id, String name, String[] subCategories) {

        Category category = new Category();
        category.mNew = false;
        category.mDirty = false;
        category.mId = id;
        category.mName = name;
        category.mSubCategories = subCategories;

        return category;
    }

    public static Category createNew() {

        Category category = new Category();
        category.mNew = true;
        category.mDirty = true;

        return category;
    }

    private Long mId;

    private String mName;

    private String[] mSubCategories;

    private Category() {
    }

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String[] getSubCategories() {
        return mSubCategories;
    }

    public String getSubCategoriesAsString() {

        StringBuilder sb = new StringBuilder();

        for (String subCategory : getSubCategories()) {

            if (sb.length() != 0) {
                sb.append("|");
            }
            sb.append(subCategory);

        }

        return sb.toString();
    }

    public String getSubCategory(int i) {

        if (i > getSubCategories().length - 1)
            return null;

        return getSubCategories()[i];

    }

    public void setId(Long id) {
        mId = id;
    }

    public void setName(String name) {

        if (null == mName || !mName.equals(name)) {
            mName = name;
            mDirty = true;
        }

    }

    public void setSubCategories(String[] subCategories) {

        if (null == mSubCategories
                || !Arrays.equals(mSubCategories, subCategories)) {
            mSubCategories = subCategories;
            mDirty = true;
        }

    }

    public int getSubCategoryCount(){
        if(null == mSubCategories)
            return 0;

        return this.mSubCategories.length;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!mId.equals(category.mId)) return false;
        if (!mName.equals(category.mName)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(mSubCategories, category.mSubCategories);

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + Arrays.hashCode(mSubCategories);
        return result;
    }
}
