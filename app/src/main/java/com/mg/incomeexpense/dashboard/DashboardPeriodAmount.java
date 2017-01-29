package com.mg.incomeexpense.dashboard;

import android.support.annotation.NonNull;

import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.Tools;

import java.util.Objects;

/**
 * Created by mario on 2016-08-23.
 */
public class DashboardPeriodAmount {

    private int mPosition;
    private Contributor mContributor = null;
    private Double mExpense = 0.0;
    private Double mIncome = 0.0;
    private Type mType = null;
    public DashboardPeriodAmount(@NonNull Integer position, @NonNull Contributor contributor, @NonNull Type type) {

        Objects.requireNonNull(position, "Parameter position of type Integer is mandatory");
        Objects.requireNonNull(contributor, "Parameter contributor of type Contributor is mandatory");

        mPosition = position;
        mContributor = contributor;
        mType = type;
    }

    public Double addIncome(Double amount) {
        return (mIncome += amount);
    }

    public Double addExpense(Double amount) {
        return (mExpense += amount);
    }

    public Contributor getContributor() {
        return mContributor;
    }

    public Integer getPosition() {
        return mPosition;
    }

    public String getExpenseAsString() {
        return Tools.formatAmount(mExpense);
    }

    public String getIncomeAsString() {
        return Tools.formatAmount(mIncome);
    }

    public Type getType() {
        return mType;
    }

    public enum Type {
        Today, Week, Month, Year, LastYear
    }
}
