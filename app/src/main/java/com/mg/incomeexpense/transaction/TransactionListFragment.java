package com.mg.incomeexpense.transaction;

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
public class TransactionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemSelectedHandler {

       /*
        Account
        Category
        Type
        Date
        Amount
        Currency
        ExchangeRate
        PaymentMethod
        Note
         */

    public static final int COL_DATE = 1;
    public static final int COL_ACCOUNT_ID = 2;
    public static final int COL_CATEGORY = 3;
    public static final int COL_COLUMN_TYPE = 4;
    public static final int COL_COLUMN_DATE = 5;
    public static final int COL_COLUMN_AMOUNT = 6;
    public static final int COL_COLUMN_CURRENCY = 7;
    public static final int COL_COLUMN_EXCHANGERATE = 8;
    public static final int COL_COLUMN_PAYMENT_METHOD_ID = 9;
    public static final int COL_COLUMN_NOTE = 10;

    private static final String[] TRANSACTION_COLUMNS = {
            IncomeExpenseContract.TransactionEntry._ID,
            IncomeExpenseContract.TransactionEntry.COLUMN_ACCOUNT_ID,
            IncomeExpenseContract.TransactionEntry.COLUMN_CATEGORY,
            IncomeExpenseContract.TransactionEntry.COLUMN_TYPE,
            IncomeExpenseContract.TransactionEntry.COLUMN_DATE,
            IncomeExpenseContract.TransactionEntry.COLUMN_AMOUNT,
            IncomeExpenseContract.TransactionEntry.COLUMN_CURRENCY,
            IncomeExpenseContract.TransactionEntry.COLUMN_EXCHANGERATE,
            IncomeExpenseContract.TransactionEntry.COLUMN_PAYMENTMETHOD_ID,
            IncomeExpenseContract.TransactionEntry.COLUMN_NOTE
    };
    private static final String LOG_TAG = TransactionListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";
    private static final int TRANSACTION_LOADER = 0;

    private final List mListeners = new ArrayList<>();

    private TransactionListAdapter mTransactionAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;


    public TransactionListFragment() {
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

                    Transaction transaction = Transaction.create(cursor,TransactionListFragment.this.getActivity().getContentResolver());

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

        return new CursorLoader(getActivity(),
                uri,
                TRANSACTION_COLUMNS,
                null,
                null,
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
