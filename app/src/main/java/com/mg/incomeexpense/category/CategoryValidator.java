package com.mg.incomeexpense.category;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        messages.put(R.string.validation_category_mandatory, context.getString(R.string.validation_category_mandatory));
        messages.put(R.string.validation_category_duplicate, context.getString(R.string.validation_category_duplicate));

        return new CategoryValidator(names, messages);
    }

    private boolean hasDuplicate(List<String> categories) {
        String stringToCompare = null;
        List<String> listToValidate = new ArrayList<>(categories);

        Collections.sort(listToValidate, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });

        for (String category : listToValidate) {
            if (null == stringToCompare) {
                stringToCompare = category;
            } else {
                if (stringToCompare.equals(category)) {
                    return true;
                }
                stringToCompare = category;
            }
        }

        return false;

    }

    public ValidationStatus Validate(Object objectToValidate) {

        List<String> messages = new ArrayList<>();

        if (!(objectToValidate instanceof ArrayList)) {
            return ValidationStatus.create("Wrong object type, must be ArrayList.");
        }

        List<String> categories = (ArrayList<String>) objectToValidate;

        if (categories.size() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_category_mandatory));
        } else if (hasDuplicate(categories)) {
            messages.add(mValidationMessages.get(R.string.validation_category_duplicate));
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
                // TODO Fix Messages
                message = String.format(mValidationMessages.get(R.string.error_foreign_key_constraint), mValidationMessages.get(R.string.account), category.getName());
                break;
            }
        }

        return ValidationStatus.create(message);
    }

}

