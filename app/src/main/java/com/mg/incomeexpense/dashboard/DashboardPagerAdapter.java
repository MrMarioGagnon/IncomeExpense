package com.mg.incomeexpense.dashboard;

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
public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    List<Account> mAccount;

    public DashboardPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Account> accounts) {
        super(fm);

        Objects.requireNonNull(accounts, "Parameter accounts of type List<Account> is mandatory");

        mAccount = accounts;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAccount.get(position));

        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return mAccount.size();
    }

}