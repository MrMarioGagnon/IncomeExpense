package com.mg.incomeexpense.transaction;

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
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.FragmentListBase;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionListFragment extends FragmentListBase {

    private static final String SELECTED_KEY = "selected_position";
    private static final int TRANSACTION_LOADER = 0;

    private TransactionListAdapter mTransactionAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;

    private Account mSelectedAccount;

    public TransactionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mSelectedAccount = (Account) bundle.getSerializable("account");
        Objects.requireNonNull(mSelectedAccount, "An Account object is mandatory");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTransactionAdapter = new TransactionListAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.transaction_list_fragment, container, false);

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
        getLoaderManager().initLoader(TRANSACTION_LOADER, null, this);
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

        mListView = (ListView) v.findViewById(R.id.list_view_transaction);
        mListView.setAdapter(mTransactionAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (null != cursor) {

                    Transaction transaction = Transaction.create(cursor, TransactionListFragment.this.getActivity());

                    TransactionListFragment.this.notifyListener(new ItemSelectedEvent(transaction));

                }

                mPosition = position;
            }
        });

        mListView.setEmptyView(v.findViewById(R.id.text_view_no_transaction));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // Sort order:  Ascending, by date.
        String sortOrder = IncomeExpenseContract.TransactionEntry.COLUMN_DATE + " DESC";

        Uri uri = IncomeExpenseContract.TransactionEntry.CONTENT_URI;

        String selection = null;
        String[] selectionArgs = null;

        if (mSelectedAccount != null) {
            selection = String.format("%1$s=?", IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID);
            selectionArgs = new String[]{mSelectedAccount.getId().toString()};
        }

        return new CursorLoader(getActivity(),
                uri,
                null,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTransactionAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTransactionAdapter.swapCursor(null);
    }

}
