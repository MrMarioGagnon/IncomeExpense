package com.mg.incomeexpense.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.DateUtil;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.TransactionListActivity;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-08-16.
 */
public class DashboardFragment extends Fragment {

    private DashboardSectionAdapter mDataAdapter;
    private Account mAccount;
    private ListView mListViewData;

    public DashboardFragment() {

    }

    private void ShowTransactionList(DashboardPeriodAmount.Type type) {

        LocalDate dFromDate = null;
        LocalDate dToDate = null;
        LocalDate dNow = LocalDate.now();
        switch (type) {
            case Today:
                dFromDate = dNow;
                dToDate = dNow;
                break;
            case Week:
                dFromDate = DateUtil.getFirstDateOfWeek(dNow);
                dToDate = DateUtil.getLastDateOfWeek(dNow);
                break;
            case Month:
                dFromDate = DateUtil.getFirstDateOfMonth(dNow);
                dToDate = DateUtil.getLastDateOfMonth(dNow);
                break;
            case Year:
                dFromDate = DateUtil.getFirstDateOfYear(LocalDate.now());
                dToDate = DateUtil.getLastDateOfYear(LocalDate.now());
                break;
            case LastYear:
                dFromDate = DateUtil.getFirstDateOfYear(dNow.minusYears(1));
                dToDate = DateUtil.getLastDateOfYear(dNow.minusYears(1));
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("account", mAccount);
        bundle.putSerializable("fromDate", dFromDate);
        bundle.putSerializable("toDate", dToDate);
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

//        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_dashboard);
//        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (mAccount.getId() != 0)
//                    ShowTransactionList();
//                return false;
//            }
//        });

        mListViewData = (ListView) rootView.findViewById(R.id.list_view_data);
        mListViewData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAccount.getId() != 0) {
                    DashboardPeriodAmount dpa = mDataAdapter.getItem(position);
                    ShowTransactionList(dpa.getType());
                }
                return false;
            }
        });

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

            List<DashboardPeriodAmount> periodsAmount = new ArrayList<>();
            periodsAmount.addAll(data.todayData);
            periodsAmount.addAll(data.thisWeekData);
            periodsAmount.addAll(data.thisMonthData);
            periodsAmount.addAll(data.thisYearData);
            if (mAccount.getDisplayLastYearData())
                periodsAmount.addAll(data.lastYearData);

            mDataAdapter = new DashboardSectionAdapter(getActivity(), periodsAmount);
            mListViewData.setAdapter(mDataAdapter);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}

