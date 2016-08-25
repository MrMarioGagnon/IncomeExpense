package com.mg.incomeexpense.transaction;

import java.util.List;

/**
 * Created by mario on 2016-08-24.
 */
public class DashboardData {
    public List<TransactionAmountTotal> todayData;
    public List<TransactionAmountTotal> thisWeekData;
    public List<TransactionAmountTotal> thisMonthData;
    public List<TransactionAmountTotal> thisYearData;

    public DashboardData(List<TransactionAmountTotal> today, List<TransactionAmountTotal> thisWeek, List<TransactionAmountTotal> thisMonth, List<TransactionAmountTotal> thisYear) {
        todayData = today;
        thisWeekData = thisWeek;
        thisMonthData = thisMonth;
        thisYearData = thisYear;
    }
}
