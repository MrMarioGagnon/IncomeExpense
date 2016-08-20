package com.mg.incomeexpense.transaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.transaction.DashboardFragment;

import java.util.List;

/**
 * Created by mario on 2016-08-16.
 */
public class TransactionDashboardPagerAdapter extends FragmentStatePagerAdapter {

    List<Account> mAccount;

    public TransactionDashboardPagerAdapter(FragmentManager fm, List<Account> accounts) {
        super(fm);
        mAccount = accounts;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAccount.get(position));

        DashboardFragment tab = new DashboardFragment();
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