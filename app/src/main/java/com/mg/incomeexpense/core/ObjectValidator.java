package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-21.
 */
public interface ObjectValidator {
    ValidationStatus Validate(ObjectBase objectToValidate);
}
