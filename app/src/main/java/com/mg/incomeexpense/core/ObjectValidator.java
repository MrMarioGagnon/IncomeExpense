package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

/**
 * Created by mario on 2016-07-21.
 */
public interface ObjectValidator {
    ValidationStatus Validate(@NonNull ObjectBase objectToValidate);
}
