package com.mg.incomeexpense.category;

import android.content.Context;
import android.support.annotation.NonNull;

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
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class CategoryValidator implements ObjectValidator {

    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public CategoryValidator(@NonNull List<String> names, @NonNull Map<Integer, String> validationMessages) {

        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");
        Objects.requireNonNull(validationMessages, "Parameter validationMessages of type Map<Integer, String> is mandatory");

        mNames = names;
        mValidationMessages = validationMessages;
    }


    public static CategoryValidator create(@NonNull Context context, @NonNull List<String> names) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(names, "Parameter names of type List<String> is mandatory");

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_remove_category_in_use, context.getString(R.string.validation_remove_category_in_use));

        return new CategoryValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        if (null == name)
            return false;

        return mNames.contains(name.toUpperCase());

    }


    public ValidationStatus Validate(@NonNull Object objectToValidate) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

        if (!(objectToValidate instanceof Category)) {
            return ValidationStatus.create("Wrong object type, must be Category.");
        }

        List<String> messages = new ArrayList<>();

        Category category = (Category) objectToValidate;
        String name = category.getName();

        if (name.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_name_mandatory));
        } else if (isNameExists(name)) {
            messages.add(mValidationMessages.get(R.string.validation_name_already_exists));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

    public ValidationStatus canDelete(@NonNull ObjectBase objectToValidate, @NonNull List<Account> accounts) {

        Objects.requireNonNull(objectToValidate, "Parameter objectToValidate of type ObjectBase is mandatory");

        if (!(objectToValidate instanceof Category)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Objects.requireNonNull(accounts, "Parameter accounts of type List<accounts> is mandatory");

        Category category = (Category) objectToValidate;
        String message = "";

        for (Account account : accounts) {
            if (account.getCategories().contains(objectToValidate)) {
                message = String.format(mValidationMessages.get(R.string.validation_remove_category_in_use));
                break;
            }
        }

        return ValidationStatus.create(message);
    }

}

