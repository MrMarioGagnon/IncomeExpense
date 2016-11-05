package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.Objects;

/**
 * Created by mario on 2016-08-18.
 */
public class DateUtil {

    public static LocalDate getFirstDateOfWeek(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        // Premiere journee de la semaine commence le lundi, meme si lundi est dans le mois precedent
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.MONDAY)
            return date;

        return date.minusDays(dayOfWeek.getValue() - 1);
    }

    public static LocalDate getLastDateOfWeek(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        // Derniere journee de la semaine est le dimanche, meme si le dimanche est dans le mois suivant
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY)
            return date;

        return date.plusDays(7 - dayOfWeek.getValue());
    }

    public static LocalDate getFirstDateOfMonth(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

    @NonNull
    public static LocalDate getLastDateOfMonth(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type LocalDate is mandatory");

        return LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());

    }

    public static LocalDate getFirstDateOfYear(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        return LocalDate.of(date.getYear(), Month.JANUARY, 1);

    }

    public static LocalDate getLastDateOfYear(@NonNull LocalDate date) {

        Objects.requireNonNull(date, "Parameter date of type Date is mandatory");

        return LocalDate.of(date.getYear(), Month.DECEMBER, 31);

    }
}
