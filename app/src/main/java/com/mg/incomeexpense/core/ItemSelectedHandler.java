package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

/**
 * Created by mario on 2016-07-20.
 */
public interface ItemSelectedHandler {
    void addListener(@NonNull ItemSelectedListener listener);

    void notifyListener(@NonNull ItemSelectedEvent event);
}
