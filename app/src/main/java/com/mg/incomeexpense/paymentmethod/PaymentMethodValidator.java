package com.mg.incomeexpense.paymentmethod;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class PaymentMethodValidator implements ObjectValidator {

    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public PaymentMethodValidator(@NonNull List<String> names, @NonNull Map<Integer, String> validationMessages) {

        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");
        Objects.requireNonNull(validationMessages, "Parameter validationMessages of type Map<Integer, String> is mandatory");

        mNames = names;
        mValidationMessages = validationMessages;
    }

    public static PaymentMethodValidator create(@NonNull Context context, @NonNull List<String> names) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_currency_mandatory, context.getString(R.string.validation_currency_mandatory));
        messages.put(R.string.validation_exchange_rate_mandatory, context.getString(R.string.validation_exchange_rate_mandatory));

        return new PaymentMethodValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        if (null == name)
            return false;

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(@NonNull ObjectBase objectToValidate) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

        List<String> messages = new ArrayList<>();

        if (!(objectToValidate instanceof PaymentMethod)) {
            return ValidationStatus.create("Wrong object type.");
        }

        PaymentMethod paymentMethod = (PaymentMethod) objectToValidate;
        String name = paymentMethod.getName().trim();
        String currency = paymentMethod.getCurrency().trim();
        Double exchangeRate = paymentMethod.getExchangeRate();

        if (name.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_name_mandatory));
        } else if (isNameExists(name)) {
            messages.add(mValidationMessages.get(R.string.validation_name_already_exists));
        }

        if (currency.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_currency_mandatory));
        }

        if (exchangeRate <= 0) {
            messages.add(mValidationMessages.get(R.string.validation_exchange_rate_mandatory));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

    public Boolean canDelete(List<Transaction> transactions) {
        return transactions.size() == 0;
    }

}
