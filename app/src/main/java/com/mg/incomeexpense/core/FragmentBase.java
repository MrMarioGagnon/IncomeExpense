package com.mg.incomeexpense.core;


import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-10-14.
 */

public class FragmentBase extends Fragment implements ItemStateChangeHandler {

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();

    @Override
    public void addListener(ItemStateChangeListener listener) {

        if (null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    @Override
    public void notifyListener(ItemStateChangeEvent event) {
        if (null == event)
            return;

        for (Object item : mListeners) {
            ((ItemStateChangeListener) item).onItemStateChange(event);
        }

    }

    public void displayMessage(@NonNull View view, @NonNull String message){

        if(null == view || null == message || message.length() == 0)
            return;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT );
        snackbar.show();

    }

}
