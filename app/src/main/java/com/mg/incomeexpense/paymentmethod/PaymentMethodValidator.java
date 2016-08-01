package com.mg.incomeexpense.paymentmethod;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 2016-07-23.
 */
public class PaymentMethodValidator implements ObjectValidator {

    private static final String LOG_TAG = PaymentMethodValidator.class.getSimpleName();
    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public PaymentMethodValidator(@NonNull List<String> names, @NonNull Map<Integer, String> validationMessages) {
        mNames = names;
        mValidationMessages = validationMessages;
    }

    public static PaymentMethodValidator create(Context context, List<String> names) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_currency_mandatory, context.getString(R.string.validation_currency_mandatory));
        messages.put(R.string.validation_exchange_rate_mandatory, context.getString(R.string.validation_exchange_rate_mandatory));
        messages.put(R.string.validation_contributors_mandatory, context.getString(R.string.validation_contributors_mandatory));

        return new PaymentMethodValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(@NonNull ObjectBase objectToValidate){

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

        Log.d(LOG_TAG, exchangeRate.toString());
        if(exchangeRate <= 0){
            messages.add(mValidationMessages.get(R.string.validation_exchange_rate_mandatory));
        }

        if (paymentMethod.getContributors().size() == 0) {
            messages.add((mValidationMessages.get(R.string.validation_contributors_mandatory)));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

}
