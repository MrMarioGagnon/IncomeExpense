package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by mario on 2016-07-21.
 */
public class ValidationStatus {

    private String mMessage = "";

    private ValidationStatus(String message) {
        mMessage = message;
    }

    public static ValidationStatus create(@NonNull String message) {
        Objects.requireNonNull(message, "Parameter message of type String is mandatory");
        return new ValidationStatus(message);
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isValid() {
        return mMessage.length() == 0;
    }
}