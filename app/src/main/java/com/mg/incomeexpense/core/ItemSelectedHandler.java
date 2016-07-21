package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-20.
 */
public interface ItemSelectedHandler {
    void addListener(ItemSelectedListener listener);

    void notifyListener(ItemSelectedEvent event);
}
