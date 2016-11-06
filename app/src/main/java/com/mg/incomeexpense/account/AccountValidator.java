package com.mg.incomeexpense.account;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.contributor.Contributor;
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
public class AccountValidator implements ObjectValidator {

    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public AccountValidator(@NonNull List<String> names, @NonNull Map<Integer, String> validationMessages) {

        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");
        Objects.requireNonNull(validationMessages, "Parameter validationMessages of type Map<Integer, String> is mandatory");

        mNames = names;
        mValidationMessages = validationMessages;
    }

    public static AccountValidator create(@NonNull Context context, @NonNull List<String> names) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_currency_mandatory, context.getString(R.string.validation_currency_mandatory));
        messages.put(R.string.validation_contributors_mandatory, context.getString(R.string.validation_contributors_mandatory));
        messages.put(R.string.validation_category_mandatory, context.getString(R.string.validation_category_mandatory));
        messages.put(R.string.validation_budget_must_be_positive, context.getString(R.string.validation_budget_must_be_positive));
        messages.put(R.string.validation_remove_contributor_from_account, context.getString(R.string.validation_remove_contributor_from_account));
        messages.put(R.string.error_foreign_key_constraint, context.getString(R.string.error_foreign_key_constraint));
        messages.put(R.string.account, context.getString(R.string.account));

        return new AccountValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        if (null == name)
            return false;

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(@NonNull Object objectToValidate) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

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

    public ValidationStatus canDelete(@NonNull ObjectBase objectToValidate, @NonNull List<Transaction> transactions) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

        if (!(objectToValidate instanceof Account)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Objects.requireNonNull(transactions, "Parameter accounts of type List<transactions> is mandatory");

        Account account = (Account) objectToValidate;
        String message = "";

        if (transactions.size() != 0) {
            message = String.format(mValidationMessages.get(R.string.error_foreign_key_constraint), mValidationMessages.get(R.string.account), account.getName());
        }

        return ValidationStatus.create(message);
    }

    public ValidationStatus canRemoveContributor(@NonNull ObjectBase objectToValidate, @NonNull List<Contributor> newContributors, @NonNull List<Transaction> transactions) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");
        Objects.requireNonNull(newContributors, "Parameter newContributors of type List<Contributor> is mandatory");
        Objects.requireNonNull(transactions, "Parameter transactions of type List<Transaction> is mandatory");

        if (!(objectToValidate instanceof Account)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Account account = (Account) objectToValidate;
        String message = "";

        for (Contributor contributor : account.getContributors()) {
            if (!newContributors.contains(contributor)) {
                for (Transaction transaction : transactions) {
                    if (transaction.getContributors().contains(contributor)) {
                        message = String.format(mValidationMessages.get(R.string.validation_remove_contributor_from_account), contributor.getName());
                        break;
                    }
                }
            }
        }
        return ValidationStatus.create(message);
    }
}

