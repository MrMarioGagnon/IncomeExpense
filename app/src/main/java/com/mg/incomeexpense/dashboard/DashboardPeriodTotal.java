package com.mg.incomeexpense.dashboard;

import java.util.List;

/**
 * Created by mario on 2016-08-24.
 */
public class DashboardPeriodTotal {
    public List<DashboardPeriodAmount> todayData;
    public List<DashboardPeriodAmount> thisWeekData;
    public List<DashboardPeriodAmount> thisMonthData;
    public List<DashboardPeriodAmount> thisYearData;
    public List<DashboardPeriodAmount> lastYearData;

    public DashboardPeriodTotal(List<DashboardPeriodAmount> today, List<DashboardPeriodAmount> thisWeek, List<DashboardPeriodAmount> thisMonth, List<DashboardPeriodAmount> thisYear, List<DashboardPeriodAmount> lastYear) {
        todayData = today;
        thisWeekData = thisWeek;
        thisMonthData = thisMonth;
        thisYearData = thisYear;
        lastYearData = lastYear;
    }
}
