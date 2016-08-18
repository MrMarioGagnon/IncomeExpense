package com.mg.incomeexpense;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.account.Account;

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

//        TextView textView = (TextView) rootView.findViewById(R.id.textView);
//
//        textView.setText(mAccount.getName());

        return rootView;


    }
}