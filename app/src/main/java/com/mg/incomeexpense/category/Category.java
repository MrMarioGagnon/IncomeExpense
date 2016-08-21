package com.mg.incomeexpense.category;

import android.content.ContentResolver;
import android.database.Cursor;

import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by mario on 2016-08-05.
 */
public class Category extends ObjectBase implements Serializable {

    private static final String LOG_TAG = Category.class.getSimpleName();

    private String mName;
    private String[] mSubCategories;
    private String mSelectedSubCategory;

    private Category() {
    }

    public static Category create(Cursor cursor, ContentResolver contentResolver) {
        Category newInstance = new Category();
        newInstance.mNew = false;
        newInstance.mDirty = false;

        Long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_NAME));
        String subCategories = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.CategoryEntry.COLUMN_SUB_CATEGORY));

        newInstance.mId = id;
        newInstance.mName = name;
        newInstance.mSubCategories = subCategories.split("|");

        return newInstance;
    }

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

    public String getSelectedSubCategory() {
        return mSelectedSubCategory;
    }

    public String getSelectedCategory(){
        return String.format("%1$d|%2$s", getId(), getSelectedSubCategory());
    }

    public String getSelectedCategoryToDisplay(){
        return String.format("%1$s:%2$s", getName(), getSelectedSubCategory());
    }

    public void setSelectedSubCategory(String selectedSubCategory) {
        mSelectedSubCategory = selectedSubCategory;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {

        if (null == mName || !mName.equals(name)) {
            mName = name;
            mDirty = true;
        }

    }

    public String[] getSubCategories() {
        return mSubCategories;
    }

    public void setSubCategories(String[] subCategories) {

        // Make sure sub categories are ordered
        Arrays.sort(subCategories);
        if (null == mSubCategories
                || !Arrays.equals(mSubCategories, subCategories)) {
            mSubCategories = subCategories;
            mDirty = true;
        }

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

    public int getSubCategoryCount() {
        if (null == mSubCategories)
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

        if (!mName.equals(category.mName)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(mSubCategories, category.mSubCategories)) return false;
        return mSelectedSubCategory.equals(category.mSelectedSubCategory);

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + Arrays.hashCode(mSubCategories);
        result = 31 * result + mSelectedSubCategory.hashCode();
        return result;
    }
}
