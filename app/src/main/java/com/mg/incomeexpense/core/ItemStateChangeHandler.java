package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-21.
 */
public interface ItemStateChangeHandler {
    void addListener(ItemStateChangeListener listener);
    void notifyListener(ItemStateChangeEvent event);
}
