package com.mg.incomeexpense.contributor;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.FragmentListBase;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.data.IncomeExpenseContract;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorListFragment extends FragmentListBase {

    private static final String SELECTED_KEY = "selected_position";
    private static final int CONTRIBUTOR_LOADER = 0;

    private ContributorListAdapter mContributorAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContributorAdapter = new ContributorListAdapter(getActivity(), null, 0);
        // Add this line in order for this fragment to handle menu events.
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contributor_list_fragment, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_contributor);
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

        mListView.setEmptyView(rootView.findViewById(R.id.textView_no_contributor));

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        String sortOrder = String.format("LOWER(%1$s) ASC", IncomeExpenseContract.ContributorEntry.COLUMN_NAME);

        Uri contributorUri = IncomeExpenseContract.ContributorEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                contributorUri,
                null,
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

}
