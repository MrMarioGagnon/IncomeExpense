package com.mg.incomeexpense.transaction;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class TransactionValidator implements ObjectValidator {

    private final Map<Integer, String> mValidationMessages;

    public TransactionValidator(@NonNull Map<Integer, String> validationMessages) {

        Objects.requireNonNull(validationMessages, "Parameter validationMessages of type Map<Integer, String> is mandatory");

        mValidationMessages = validationMessages;
    }

    public static TransactionValidator create(@NonNull Context context) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_account_mandatory, context.getString(R.string.validation_account_mandatory));
        messages.put(R.string.validation_category_mandatory, context.getString(R.string.validation_category_mandatory));
        messages.put(R.string.validation_sub_category_mandatory, context.getString(R.string.validation_sub_category_mandatory));
        messages.put(R.string.validation_type_mandatory, context.getString(R.string.validation_type_mandatory));
        messages.put(R.string.validation_date_mandatory, context.getString(R.string.validation_date_mandatory));
        messages.put(R.string.validation_amount_mandatory, context.getString(R.string.validation_amount_mandatory));
        messages.put(R.string.validation_currency_mandatory, context.getString(R.string.validation_currency_mandatory));
        messages.put(R.string.validation_exchange_rate_mandatory, context.getString(R.string.validation_exchange_rate_mandatory));
        messages.put(R.string.validation_contributors_mandatory, context.getString(R.string.validation_contributors_mandatory));
        messages.put(R.string.validation_payment_method_mandatory, context.getString(R.string.validation_payment_method_mandatory));

        return new TransactionValidator(messages);
    }

    public ValidationStatus Validate(@NonNull Object objectToValidate) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type Object is mandatory");

        if (!(objectToValidate instanceof Transaction)) {
            return ValidationStatus.create("Wrong object type.");
        }

        List<String> messages = new ArrayList<>();
        String message;


        Transaction transaction = (Transaction) objectToValidate;

        if (transaction.getAccount() == null) {
            message = mValidationMessages.get(R.string.validation_account_mandatory);
            messages.add(message);
        }

        String category = transaction.getCategory();
        if (category == null) {
            message = mValidationMessages.get(R.string.validation_category_mandatory);
            messages.add(message);
        }

        if (transaction.getType() == null) {
            message = mValidationMessages.get(R.string.validation_type_mandatory);
            messages.add(message);
        }

        if (transaction.getDate() == null || transaction.getDate().trim().length() == 0) {
            message = mValidationMessages.get(R.string.validation_date_mandatory);
            messages.add(message);
        }

        if (transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
            message = mValidationMessages.get(R.string.validation_amount_mandatory);
            messages.add(message);
        }

        if (transaction.getCurrency() == null || transaction.getCurrency().trim().length() == 0) {
            message = mValidationMessages.get(R.string.validation_currency_mandatory);
            messages.add(message);
        }

        if (transaction.getExchangeRate() == null || transaction.getExchangeRate() <= 0.0) {
            message = mValidationMessages.get(R.string.validation_exchange_rate_mandatory);
            messages.add(message);
        }

        if (transaction.getPaymentMethod() == null) {
            message = mValidationMessages.get(R.string.validation_payment_method_mandatory);
            messages.add(message);
        }

        if (transaction.getContributors().size() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_contributors_mandatory));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

}

