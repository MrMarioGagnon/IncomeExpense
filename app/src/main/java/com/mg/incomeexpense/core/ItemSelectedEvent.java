package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-20.
 */
public class ItemSelectedEvent {

    private ObjectBase mItem;

    public ItemSelectedEvent(ObjectBase item) {
        if (null == item)
            throw new NullPointerException("Parameter item of type ObjectBase is mandatory.");

        mItem = item;
    }

    public ObjectBase getItem() {
        return mItem;
    }
}
