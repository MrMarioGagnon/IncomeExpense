package com.mg.incomeexpense.transaction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.dashboard.DashboardFragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-08-16.
 */
public class TransactionDashboardPagerAdapter extends FragmentStatePagerAdapter {

    List<Account> mAccount;

    public TransactionDashboardPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Account> accounts) {
        super(fm);

        Objects.requireNonNull(accounts, "Parameter accounts of type List<Account> is mandatory");

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

    public Account getAccount(int i) {
        return mAccount.get(i);
    }


}