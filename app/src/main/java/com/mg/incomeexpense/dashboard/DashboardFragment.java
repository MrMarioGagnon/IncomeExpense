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
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.TransactionListActivity;

import org.threeten.bp.LocalDate;

import java.util.Objects;

/**
 * Created by mario on 2016-08-16.
 */
public class DashboardFragment extends Fragment {

    private DashboardSectionAdapter mTodayAdapter;
    private DashboardSectionAdapter mThisWeekAdapter;
    private DashboardSectionAdapter mThisMonthAdapter;
    private DashboardSectionAdapter mThisYearAdapter;
    private DashboardSectionAdapter mLastYearAdapter;

    private Account mAccount;

    private ListView mListViewToday;
    private ListView mListViewThisWeek;
    private ListView mListViewThisMonth;
    private ListView mListViewThisYear;
    private ListView mListViewLastYear;

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
                if (mAccount.getId() != 0)
                    ShowTransactionList();
                return false;
            }
        });

        mListViewToday = (ListView) rootView.findViewById(R.id.list_view_today);

        mListViewThisWeek = (ListView) rootView.findViewById(R.id.list_view_this_week);
        mListViewThisMonth = (ListView) rootView.findViewById(R.id.list_view_this_month);
        mListViewThisYear = (ListView) rootView.findViewById(R.id.list_view_this_year);
        mListViewLastYear = (ListView) rootView.findViewById(R.id.list_view_last_year);

        if (!mAccount.getDisplayLastYearData()) {
            mListViewLastYear.setVisibility(View.GONE);
        }

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
            Tools.setListViewHeightBasedOnChildren(mListViewToday);

            mThisWeekAdapter = new DashboardSectionAdapter(getActivity(), data.thisWeekData);
            mListViewThisWeek.setAdapter(mThisWeekAdapter);
            Tools.setListViewHeightBasedOnChildren(mListViewThisWeek);

            mThisMonthAdapter = new DashboardSectionAdapter(getActivity(), data.thisMonthData);
            mListViewThisMonth.setAdapter(mThisMonthAdapter);
            Tools.setListViewHeightBasedOnChildren(mListViewThisMonth);

            mThisYearAdapter = new DashboardSectionAdapter(getActivity(), data.thisYearData);
            mListViewThisYear.setAdapter(mThisYearAdapter);
            Tools.setListViewHeightBasedOnChildren(mListViewThisYear);

            mLastYearAdapter = new DashboardSectionAdapter(getActivity(), data.lastYearData);
            mListViewLastYear.setAdapter(mLastYearAdapter);
            Tools.setListViewHeightBasedOnChildren(mListViewLastYear);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

