package com.mg.incomeexpense;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.DateUtil;
import com.mg.incomeexpense.core.Tools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mario on 2016-08-16.
 */
public class TabFragment1 extends Fragment
{

    private Account mAccount;

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
        View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int lastDayCurrentMonth = calendar.getMaximum(Calendar.DAY_OF_MONTH);
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


        return rootView;


    }
}