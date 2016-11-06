package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by mario on 2016-07-21.
 */
public class ItemStateChangeEvent {

    private Object mItem;
    private Boolean mIsCancelled = false;

    public ItemStateChangeEvent(@NonNull Object item, Boolean isCancelled) {

        Objects.requireNonNull(item, "Parameter item of type ObjectBase is mandatory.");

        if (item instanceof ObjectBase) {
            ObjectBase ob = (ObjectBase) item;
            if (ob.getId() == null && !ob.isNew()) {
                throw new NullPointerException("Item id is null but item is not new.");
            }
        }

        mItem = item;
        mIsCancelled = null == isCancelled ? false : isCancelled;

    }

    public Boolean isCancelled() {
        return mIsCancelled;
    }

    public Object getItem() {
        return mItem;
    }

}
