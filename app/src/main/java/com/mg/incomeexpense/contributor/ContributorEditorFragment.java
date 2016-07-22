package com.mg.incomeexpense.contributor;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedListener;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorFragment extends Fragment implements ItemStateChangeHandler{

    private static final String LOG_TAG = ContributorEditorFragment.class.getSimpleName();

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Contributor mContributor = null;

    public ContributorEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        assert bundle!= null;

        mContributor = (Contributor) bundle.getSerializable("item");
        assert mContributor != null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contributor_editor_fragment, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contributor_editor, menu);
        Log.i(LOG_TAG, "In onCreateOptionsMenu");

        if (mContributor.isNew()) {
            MenuItem mi = menu.findItem(R.id.action_delete);
            if (null != mi) {
                mi.setVisible(false);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
            case R.id.action_delete:
                Intent intent = new Intent();
                intent.putExtra("item", mContributor);
//                setResult(RESULT_OK, intent);
  //              finish();
                break;
            case android.R.id.home:
    //            setResult(RESULT_CANCELED);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @Override
    public void addListener(ItemStateChangeListener listener) {

        if(null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    @Override
    public void notifyListener(ItemStateChangeEvent event) {
        if(null == event)
            return;

        for (Object item : mListeners) {
            ((ItemStateChangeListener) item).onItemStateChange(event);
        }

    }
}
