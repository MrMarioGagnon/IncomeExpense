package com.mg.incomeexpense.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.TransactionListActivity;

import org.threeten.bp.LocalDate;

import java.util.Date;
import java.util.Objects;

/**
 * Created by mario on 2016-08-16.
 */
public class DashboardFragment extends Fragment {

    private DashboardSectionAdapter mTodayAdapter;
    private DashboardSectionAdapter mThisWeekAdapter;
    private DashboardSectionAdapter mThisMonthAdapter;
    private DashboardSectionAdapter mThisYearAdapter;

    private Account mAccount;

    private ListView mListViewToday;
    private ListView mListViewThisWeek;
    private ListView mListViewThisMonth;
    private ListView mListViewThisYear;

    public DashboardFragment() {

    }

    private void ShowTransactionList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", mAccount);
        Intent intent = new Intent(getActivity(), TransactionListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mAccount = (Account) bundle.getSerializable("item");
        Objects.requireNonNull(mAccount, "An account is mandatory");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transaction_dashboard_fragment, container, false);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_dashboard);
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mAccount.getId() != 0)
                    ShowTransactionList();
                return false;
            }
        });

        mListViewToday = (ListView) rootView.findViewById(R.id.list_view_today);
        mListViewThisWeek = (ListView) rootView.findViewById(R.id.list_view_this_week);
        mListViewThisMonth = (ListView) rootView.findViewById(R.id.list_view_this_month);
        mListViewThisYear = (ListView) rootView.findViewById(R.id.list_view_this_year);

        refresh();

        return rootView;

    }

    public void refresh() {
        new GetData().execute("");
    }

    /*
    http://stackoverflow.com/questions/9671546/asynctask-android-example
     */
    private class GetData extends AsyncTask<String, Void, DashboardPeriodTotal> {

        @Override
        protected DashboardPeriodTotal doInBackground(String... params) {

            DashboardPeriodTotal d = IncomeExpenseRequestWrapper.getDashboardData(getActivity(), mAccount, LocalDate.now());

            return d;
        }

        @Override
        protected void onPostExecute(DashboardPeriodTotal data) {

            mTodayAdapter = new DashboardSectionAdapter(getActivity(), data.todayData);
            mListViewToday.setAdapter(mTodayAdapter);

            mThisWeekAdapter = new DashboardSectionAdapter(getActivity(), data.thisWeekData);
            mListViewThisWeek.setAdapter(mThisWeekAdapter);

            mThisMonthAdapter = new DashboardSectionAdapter(getActivity(), data.thisMonthData);
            mListViewThisMonth.setAdapter(mThisMonthAdapter);

            mThisYearAdapter = new DashboardSectionAdapter(getActivity(), data.thisYearData);
            mListViewThisYear.setAdapter(mThisYearAdapter);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

