package com.mg.incomeexpense.dashboard;

import android.support.annotation.NonNull;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-08-23.
 */
public class DashboardAmountAccumulator {

    private final Hashtable<Long, DashboardPeriodAmount> mPeriodTotals;
    private final List<Contributor> mAccountContributors;
    private final Long FAKE_GRAND_TOTAL_ID = 999999L;

    public DashboardAmountAccumulator(List<Contributor> accountContributors, String date, DashboardPeriodAmount.Type type) {

        mPeriodTotals = new Hashtable<>();
        mAccountContributors = accountContributors;
        int i = 0;

        // Fake contributeur pour la ligne avec la date
        Contributor titleContributor = Contributor.createNew();
        titleContributor.setId(0L);
        titleContributor.setName(date);
        mPeriodTotals.put(0L, new DashboardPeriodAmount(++i, titleContributor, type));

        // Contributeurs du compte
        for (Contributor contributor : mAccountContributors) {
            mPeriodTotals.put(contributor.getId(), new DashboardPeriodAmount(++i, contributor, type));
        }

        // Fake contributeur pour le grand total de la date
        Contributor totalContributor = Contributor.createNew();
        totalContributor.setId(FAKE_GRAND_TOTAL_ID);
        totalContributor.setName("Total");
        mPeriodTotals.put(totalContributor.getId(), new DashboardPeriodAmount(++i, totalContributor, type));

    }

    public List<DashboardPeriodAmount> getPeriodAmountTotal() {

        List<DashboardPeriodAmount> items = new ArrayList<>();

        Enumeration<DashboardPeriodAmount> e = mPeriodTotals.elements();

        while (e.hasMoreElements()) {
            items.add(e.nextElement());
        }

        Collections.sort(items, new Comparator<DashboardPeriodAmount>() {
            @Override
            public int compare(DashboardPeriodAmount lhs, DashboardPeriodAmount rhs) {
                return Integer.compare(lhs.getPosition(), rhs.getPosition());
            }
        });

        return items;

    }

    public void Add(@NonNull List<Contributor> transactionContributors, @NonNull Transaction.TransactionType type, Double amount) {

        if (null == amount)
            return;

        Objects.requireNonNull(transactionContributors, "Parameter transactionContributors of type List<Contributor> is mandatory");
        Objects.requireNonNull(type, "Parameter type of type TransactionType is mandatory");

        int divider = transactionContributors.size();
        Double splitAmount = amount / divider;

        DashboardPeriodAmount periodGrandTotal = mPeriodTotals.get(FAKE_GRAND_TOTAL_ID);
        DashboardPeriodAmount contributorAccumulator;
        for (Contributor contributor : mAccountContributors) {

            if (!transactionContributors.contains(contributor))
                continue;

            contributorAccumulator = mPeriodTotals.get(contributor.getId());
            if (contributorAccumulator != null) {
                switch (type) {
                    case Expense:
                        contributorAccumulator.addExpense(splitAmount);
                        if (null != periodGrandTotal)
                            periodGrandTotal.addExpense(splitAmount);
                        break;
                    case Income:
                        contributorAccumulator.addIncome(splitAmount);
                        if (null != periodGrandTotal)
                            periodGrandTotal.addIncome(splitAmount);
                        break;
                }
            }
        }

    }

}
