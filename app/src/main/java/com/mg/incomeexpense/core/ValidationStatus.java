package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-21.
 */
public class ValidationStatus {

    private String mMessage = "";

    private ValidationStatus(String message) {
        mMessage = message;
    }

    public static ValidationStatus create(String message) {
        return new ValidationStatus(message);
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isValid() {
        return mMessage.length() == 0;
    }
}