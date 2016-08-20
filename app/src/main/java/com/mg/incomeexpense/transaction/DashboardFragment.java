package com.mg.incomeexpense.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.DateUtil;
import com.mg.incomeexpense.core.Tools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mario on 2016-08-16.
 */
public class DashboardFragment extends Fragment
{

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();

    private Account mAccount;
    private View.OnClickListener mOnClickSectionListener;

    public DashboardFragment() {

        mOnClickSectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTransactionList();
            }
        };
    }

    private void ShowTransactionList() {
        Intent intent = new Intent(getActivity(), TransactionListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (null == bundle)
            throw new NullPointerException("A bundle is mandatory");

        mAccount = (Account) bundle.getSerializable("item");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transaction_dashboard_fragment, container, false);

        Calendar calendar = Calendar.getInstance();
        String fromDate;
        String toDate;

        TextView textViewToday = (TextView)rootView.findViewById(R.id.text_view_today_date);
        textViewToday.setText(Tools.formatDate( calendar.getTime(), "yyyy-MM-dd" ) );

        TextView textViewThisWeek = (TextView)rootView.findViewById(R.id.text_view_this_week_date);
        fromDate = Tools.formatDate(DateUtil.getFirstDateOfWeek( new Date() ).getTime(), "yyyy-MM-dd" );
        toDate = Tools.formatDate(DateUtil.getLastDateOfWeek( new Date() ).getTime(), "yyyy-MM-dd" );
        textViewThisWeek.setText( String.format("%1$s - %2$s", fromDate, toDate)  );

        TextView textViewThisMonth = (TextView)rootView.findViewById(R.id.text_view_this_month_date);
        fromDate = Tools.formatDate( DateUtil.getFirstDateOfMonth(new Date()).getTime(), "yyyy-MM-dd" );
        toDate = Tools.formatDate( DateUtil.getLastDateOfMonth(new Date()).getTime() , "yyyy-MM-dd" );
        textViewThisMonth.setText( String.format("%1$s - %2$s", fromDate, toDate)  );

        TextView textViewThisYear = (TextView)rootView.findViewById(R.id.text_view_this_year_date);
        fromDate = Tools.formatDate( DateUtil.getFirstDateOfYear( new Date() ).getTime(), "yyyy-MM-dd" );
        toDate = Tools.formatDate( DateUtil.getLastDateOfYear( new Date() ).getTime(), "yyyy-MM-dd" );
        textViewThisYear.setText( String.format("%1$s - %2$s", fromDate, toDate)  );

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_today);
        linearLayout.setOnClickListener(mOnClickSectionListener);


        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_this_week);
        linearLayout.setOnClickListener(mOnClickSectionListener);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_this_month);
        linearLayout.setOnClickListener(mOnClickSectionListener);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_this_year);
        linearLayout.setOnClickListener(mOnClickSectionListener);


        return rootView;


    }
}