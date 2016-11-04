package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by mario on 2016-07-20.
 */
public class ItemSelectedEvent {

    private ObjectBase mItem;

    public ItemSelectedEvent(@NonNull ObjectBase item) {

        Objects.requireNonNull(item, "Parameter item of type ObjectBase is mandatory.");

        mItem = item;
    }

    public ObjectBase getItem() {
        return mItem;
    }
}
