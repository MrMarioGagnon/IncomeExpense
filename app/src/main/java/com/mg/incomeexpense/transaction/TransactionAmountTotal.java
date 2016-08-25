package com.mg.incomeexpense.transaction;

import com.mg.incomeexpense.contributor.Contributor;

/**
 * Created by mario on 2016-08-23.
 */
public class TransactionAmountTotal {

    public int mPosition;
    public Contributor contributor = null;
    public Double expense = 0.0;
    public Double income = 0.0;

    public TransactionAmountTotal(Integer position, Contributor contributor) {
        mPosition = position;
        this.contributor = contributor;
    }
}
