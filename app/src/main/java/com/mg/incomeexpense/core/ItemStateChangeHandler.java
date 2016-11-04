package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

/**
 * Created by mario on 2016-07-21.
 */
public interface ItemStateChangeHandler {
    void addListener(@NonNull ItemStateChangeListener listener);

    void notifyListener(@NonNull ItemStateChangeEvent event);
}
