package com.mg.incomeexpense.contributor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mario on 2016-07-21.
 */
public class ContributorValidator implements ObjectValidator {
    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public ContributorValidator(@NonNull List<String> names, @NonNull Map<Integer, String> validationMessages) {

        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");
        Objects.requireNonNull(validationMessages, "Parameter validationMessages of type Map<Integer, String> is mandatory");

        mNames = names;
        mValidationMessages = validationMessages;

    }

    public static ContributorValidator create(@NonNull Context context, @NonNull List<String> names) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.error_foreign_key_constraint_contributor1, context.getString(R.string.error_foreign_key_constraint_contributor1));
        messages.put(R.string.error_foreign_key_constraint_contributor2, context.getString(R.string.error_foreign_key_constraint_contributor2));
        messages.put(R.string.error_foreign_key_constraint_contributor3, context.getString(R.string.error_foreign_key_constraint_contributor3));
        return new ContributorValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        if (null == name)
            return false;

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(@NonNull Object objectToValidate) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type Object is mandatory");

        if (!(objectToValidate instanceof Contributor)) {
            return ValidationStatus.create("Wrong object type.");
        }

        List<String> messages = new ArrayList<>();

        Contributor contributor = (Contributor) objectToValidate;
        String name = contributor.getName().trim();

        if (name.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_name_mandatory));
        } else if (isNameExists(name)) {
            messages.add(mValidationMessages.get(R.string.validation_name_already_exists));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

    public ValidationStatus canDelete(@NonNull ObjectBase objectToValidate, @NonNull List<Account> accounts, @NonNull List<PaymentMethod> paymentMethods) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

        if (!(objectToValidate instanceof Contributor)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Objects.requireNonNull(accounts, "Parameter accounts of type List<Account> is mandatory");
        Objects.requireNonNull(paymentMethods, "Parameter paymentMethods of type List<PaymentMethod> is mandatory");

        Contributor contributor = (Contributor) objectToValidate;
        List<String> messages = new ArrayList<>();

        for (Account account : accounts) {

            if (account.getContributors().contains(contributor)) {
                messages.add(mValidationMessages.get(R.string.error_foreign_key_constraint_contributor1));
                messages.add(mValidationMessages.get(R.string.error_foreign_key_constraint_contributor2));
                break;
            }
        }

        for (PaymentMethod paymentMethod : paymentMethods) {

            if (paymentMethod.getOwner().equals(contributor)) {
                if (messages.size() == 0) {
                    messages.add(mValidationMessages.get(R.string.error_foreign_key_constraint_contributor1));
                }
                messages.add(mValidationMessages.get(R.string.error_foreign_key_constraint_contributor3));
                break;
            }
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }
}
