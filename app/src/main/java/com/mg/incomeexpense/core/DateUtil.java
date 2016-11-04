package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by mario on 2016-08-18.
 */
public class DateUtil {

    public static Calendar getFirstDateOfWeek(@NonNull Date date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = 1;
        switch (currentDayOfWeek) {
            case Calendar.MONDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE);
                break;
            case Calendar.TUESDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 1;
                break;
            case Calendar.WEDNESDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 2;
                break;
            case Calendar.THURSDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 3;
                break;
            case Calendar.FRIDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 4;
                break;
            case Calendar.SATURDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 5;
                break;
            case Calendar.SUNDAY:
                firstDayOfWeek = calendar.get(Calendar.DATE) - 6;
                break;
        }
        firstDayOfWeek = firstDayOfWeek < 1 ? 1 : firstDayOfWeek;
        calendar.set(currentYear, currentMonth, firstDayOfWeek);
        return calendar;
    }

    public static Calendar getLastDateOfWeek(@NonNull Date date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Calendar calendar = DateUtil.getFirstDateOfWeek(date);
        int day = calendar.get(Calendar.DATE);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int maxDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        day = day + 6 > maxDay ? maxDay : day + 6;
        calendar.set(year, month, day);

        return calendar;
    }

    public static Calendar getFirstDateOfMonth(@NonNull Date date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        calendar.set(currentYear, currentMonth, 1);

        return calendar;
    }

    @NonNull
    public static LocalDate getLastDateOfMonth(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type LocalDate is mandatory");

        return LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());

    }

    public static Calendar getFirstDateOfYear(@NonNull Date date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.set(currentYear, Calendar.JANUARY, 1);

        return calendar;
    }

    public static Calendar getLastDateOfYear(@NonNull Date date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.set(currentYear, Calendar.DECEMBER, 31);

        return calendar;
    }
}
