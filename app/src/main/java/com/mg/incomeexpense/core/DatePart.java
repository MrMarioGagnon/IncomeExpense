package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

/**
 * Created by mario on 2016-07-21.
 */
public class DatePart {

    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;

    public DatePart(@NonNull Integer year, @NonNull Integer month, @NonNull Integer day) {

        mYear = year;
        mMonth = month;
        mDay = day;

    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DatePart other = (DatePart) obj;
        if (mDay != other.mDay)
            return false;
        if (mMonth != other.mMonth)
            return false;
        return mYear == other.mYear;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return mDay;
    }

    /**
     * @param day the day to set
     */
    public void setDay(Integer day) {
        mDay = day;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return mMonth;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(Integer month) {
        mMonth = month;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return mYear;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        mYear = year;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mDay;
        result = prime * result + mMonth;
        result = prime * result + mYear;
        return result;
    }

    @Override
    public String toString() {
        return "DatePart [mYear=" + mYear + ", mMonth=" + mMonth + ", mDay="
                + mDay + "]";
    }

}