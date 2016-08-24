package com.mg.incomeexpense.transaction;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by mario on 2016-08-23.
 */
public class IncExpAcc {

    private final Hashtable<Long, IncomeExpenseTotal> mTotals;
    public final IncomeExpenseTotal mTotal;

    public IncExpAcc(List<Contributor> contributors){

        mTotals = new Hashtable<>();
        for(Contributor contributor : contributors) {
                mTotals.put(contributor.getId(), new IncomeExpenseTotal());
        }
        mTotals.put(999999L, new IncomeExpenseTotal());
        mTotal = mTotals.get(999999L);

    }

    public void Add(List<Contributor> paymentMethodContributors, Transaction.TransactionType type, Double amount){

        int divider = paymentMethodContributors.size();
        Double splitAmount = amount / divider;

        IncomeExpenseTotal total;
        for(Contributor contributor:paymentMethodContributors) {
            total = mTotals.get(contributor.getId());
            if(total != null){
                switch(type){
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
