package com.mg.incomeexpense.core;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-10-26.
 */

public class FragmentListBase extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemSelectedHandler {

    private final List mListeners = new ArrayList<>();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void addListener(ItemSelectedListener listener) {

        if (null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    @Override
    public void notifyListener(ItemSelectedEvent event) {

        if (null == event)
            return;

        for (Object item : mListeners) {
            ((ItemSelectedListener) item).onItemSelected(event);
        }

    }
}
