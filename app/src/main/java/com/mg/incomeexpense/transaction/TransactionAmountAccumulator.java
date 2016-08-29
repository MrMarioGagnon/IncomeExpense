package com.mg.incomeexpense.transaction;

import com.mg.incomeexpense.contributor.Contributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by mario on 2016-08-23.
 */
public class TransactionAmountAccumulator {

    private final String mDate;
    private final Hashtable<Long, TransactionAmountTotal> mPeriodTotals;
    private final TransactionAmountTotal mTotal;
    private final List<Contributor> mContributors;

    public TransactionAmountAccumulator(List<Contributor> contributors, String date) {

        mDate = date;
        mPeriodTotals = new Hashtable<>();
        mContributors = contributors;
        int i = 0;

        Contributor titleContributor = Contributor.createNew();
        titleContributor.setId(0L);
        titleContributor.setName(date);
        mPeriodTotals.put(0L, new TransactionAmountTotal(++i, titleContributor));
        for (Contributor contributor : mContributors) {

            mPeriodTotals.put(contributor.getId(), new TransactionAmountTotal(++i, contributor));
        }

        Contributor totalContributor = Contributor.createNew();
        totalContributor.setId(999999L);
        totalContributor.setName("Total");

        mPeriodTotals.put(totalContributor.getId(), new TransactionAmountTotal(++i, totalContributor));
        mTotal = mPeriodTotals.get(totalContributor.getId());

    }

    public List<TransactionAmountTotal> getTransactionAmountTotal() {

        List<TransactionAmountTotal> items = new ArrayList<>();

        Enumeration<TransactionAmountTotal> e = mPeriodTotals.elements();

        while (e.hasMoreElements()) {
            items.add(e.nextElement());
        }

        Collections.sort(items, new Comparator<TransactionAmountTotal>() {
            @Override
            public int compare(TransactionAmountTotal lhs, TransactionAmountTotal rhs) {
                return Integer.compare(lhs.mPosition, rhs.mPosition);
            }
        });

        return items;

    }

    public void Add(List<Contributor> contributors, Transaction.TransactionType type, Double amount) {

        int divider = contributors.size();
        Double splitAmount = amount / divider;

        TransactionAmountTotal total;
        for (Contributor contributor : mContributors) {

            if(!contributors.contains(contributor))
                continue;

            total = mPeriodTotals.get(contributor.getId());
            if (total != null) {
                switch (type) {
                    case Expense:
                        total.expense += splitAmount;
                        mTotal.expense += splitAmount;
                        break;
                    case Income:
                        total.income += splitAmount;
                        mTotal.income += splitAmount;
                        break;
                }
            }
        }

    }

}
