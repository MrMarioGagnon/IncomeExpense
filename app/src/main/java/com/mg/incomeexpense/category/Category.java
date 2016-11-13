package com.mg.incomeexpense.category;

import com.mg.incomeexpense.extension.ExtensionFragmentFactory;

/**
 * Created by mario on 2016-11-13.
 */

public final class Category {

    private String mName;
    private ExtensionFragmentFactory.ExtensionType mType;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ExtensionFragmentFactory.ExtensionType getType() {
        return mType;
    }

    public void setType(ExtensionFragmentFactory.ExtensionType type) {
        this.mType = type;
    }

    @Override
    public String toString() {
        return mName;
    }
}
