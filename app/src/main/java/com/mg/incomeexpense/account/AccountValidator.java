package com.mg.incomeexpense.account;

import android.content.Context;

import com.mg.incomeexpense.R;
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
public class AccountValidator implements ObjectValidator {

    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public AccountValidator(List<String> names, Map<Integer, String> validationMessages) {
        mNames = names;
        mValidationMessages = validationMessages;
    }

    public static AccountValidator create(Context context, List<String> names) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_currency_mandatory, context.getString(R.string.validation_currency_mandatory));
        messages.put(R.string.validation_contributors_mandatory, context.getString(R.string.validation_contributors_mandatory));
        messages.put(R.string.validation_category_mandatory, context.getString(R.string.validation_category_mandatory));
        messages.put(R.string.validation_budget_must_be_positive, context.getString(R.string.validation_budget_must_be_positive));

        return new AccountValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(ObjectBase objectToValidate) {

        List<String> messages = new ArrayList<>();

        if (!(objectToValidate instanceof Account)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Account account = (Account) objectToValidate;
        String name = account.getName().trim();

        if (name.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_name_mandatory));
        } else if (isNameExists(name)) {
            messages.add(mValidationMessages.get(R.string.validation_name_already_exists));
        }

        if (account.getContributors().size() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_contributors_mandatory));
        }

        if (account.getCategories().size() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_category_mandatory));
        }

        if (null != account.getBudget() && account.getBudget() < 0) {
            messages.add(mValidationMessages.get(R.string.validation_budget_must_be_positive));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

}

