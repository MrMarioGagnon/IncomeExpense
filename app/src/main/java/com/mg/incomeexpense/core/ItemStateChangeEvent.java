package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by mario on 2016-07-21.
 */
public class ItemStateChangeEvent {

    private ObjectBase mItem;
    private Boolean mIsCancelled = false;

    public ItemStateChangeEvent(@NonNull ObjectBase item) {

        Objects.requireNonNull(item, "Parameter item of type ObjectBase is mandatory.");

        if (item.getId() == null && !item.isNew()) {
            throw new NullPointerException("Item id is null but item is not new.");
        }

        mItem = item;
        mIsCancelled = false;

    }

    public ItemStateChangeEvent() {
        mIsCancelled = true;
    }

    public Boolean isCancelled() {
        return mIsCancelled;
    }

    public ObjectBase getItem() {
        return mItem;
    }

}
