package com.mg.incomeexpense;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mg.incomeexpense.account.Account;

import java.util.List;

/**
 * Created by mario on 2016-08-16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    List<Account> mAccount;

    public PagerAdapter(FragmentManager fm, List<Account> accounts) {
        super(fm);
        mAccount = accounts;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAccount.get(position));

        TabFragment1 tab = new TabFragment1();
        tab.setArguments(bundle);

        return tab;
    }

    @Override
    public int getCount() {
        return mAccount.size();
    }

    public Account getAccount(int i){
        return mAccount.get(i);
    }
}