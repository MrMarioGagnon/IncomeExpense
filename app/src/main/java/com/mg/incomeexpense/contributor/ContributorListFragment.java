package com.mg.incomeexpense.contributor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemSelectedHandler {

    public static final int COL_NAME = 1;
    private static final String[] CONTRIBUTOR_COLUMNS = {
            IncomeExpenseContract.ContributorEntry._ID,
            IncomeExpenseContract.ContributorEntry.COLUMN_NAME
    };
    private static final String LOG_TAG = ContributorListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";
    private static final int CONTRIBUTOR_LOADER = 0;

    private final List mListeners = new ArrayList<>();

    private ContributorListAdapter mContributorAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;


    public ContributorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContributorAdapter = new ContributorListAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.contributor_list_fragment, container, false);

        setupListView(rootView);

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTRIBUTOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    private void setupListView(View v) {

        mListView = (ListView) v.findViewById(R.id.listview_contributor);
        mListView.setAdapter(mContributorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (null != cursor) {

                    long id = cursor.getLong(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(IncomeExpenseContract.ContributorEntry.COLUMN_NAME));

                    Contributor contributor = Contributor.create(id, name);

                    ContributorListFragment.this.notifyListener(new ItemSelectedEvent(contributor));

                }

                mPosition = position;
            }
        });

        mListView.setEmptyView(v.findViewById(R.id.textView_no_contributor));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // Sort order:  Ascending, by date.
        String sortOrder = IncomeExpenseContract.ContributorEntry.COLUMN_NAME + " ASC";

        Uri contributorUri = IncomeExpenseContract.ContributorEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                contributorUri,
                CONTRIBUTOR_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContributorAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContributorAdapter.swapCursor(null);
    }

    @Override
    public void addListener(ItemSelectedListener listener) {

        if(null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    @Override
    public void notifyListener(ItemSelectedEvent event) {

        if(null == event)
            return;

        for (Object item : mListeners) {
            ((ItemSelectedListener) item).onItemSelected(event);
        }
    }

}
