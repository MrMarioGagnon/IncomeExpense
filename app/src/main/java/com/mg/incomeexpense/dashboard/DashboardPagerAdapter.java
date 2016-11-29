package com.mg.incomeexpense.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mg.incomeexpense.account.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-08-16.
 */
public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    private List<Account> mAccount;
    private HashMap<Integer, DashboardFragment> mDashboards;

    public DashboardPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Account> accounts) {
        super(fm);

        Objects.requireNonNull(accounts, "Parameter accounts of type List<Account> is mandatory");

        mAccount = accounts;

        mDashboards = new HashMap<>();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAccount.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {


        if (mDashboards.containsKey(position)) {
            return mDashboards.get(position);
        }

        DashboardFragment dashboardFragment = mDashboards.get(position);
        if (null != dashboardFragment)
            return dashboardFragment;

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", mAccount.get(position));

        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);
        mDashboards.put(position, fragment);

        return fragment;
    }

    @Override
    public int getCount() {
        return mAccount.size();
    }

    public Account getAccount(int position){
        return mAccount.get(position);
    }

    public List<Account> getAccounts(){
        return mAccount;
    }
}