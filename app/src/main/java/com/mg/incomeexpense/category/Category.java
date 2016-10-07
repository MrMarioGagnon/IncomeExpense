package com.mg.incomeexpense.category;

import com.mg.incomeexpense.core.ObjectBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mario on 2016-08-05.
 */
public class Category extends ObjectBase implements Serializable {

    private List<String> mCategories;

    private Category() {
    }

    public static Category create(String[] categories) {
        Category category = new Category();
        category.mCategories = new ArrayList<>(Arrays.asList(categories));
        category.mDirty = false;
        category.mId = 1L;

        return category;

    }

    public static Category create(List<String> categories) {

        Category category = new Category();
        category.mCategories = categories;
        category.mDirty = false;
        category.mId = 1L;

        return category;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<String> getCategories() {
        return mCategories;
    }

    public void setCategories(List<String> categories) {

        // Make sure sub categories are ordered
        Collections.sort(categories);
        mCategories = categories;
    }

}
