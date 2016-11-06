package com.mg.incomeexpense.category;

import android.content.Context;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 2016-07-23.
 */
public class CategoryValidator implements ObjectValidator {

    private final Map<Integer, String> mValidationMessages;

    public CategoryValidator(Map<Integer, String> validationMessages) {
        mValidationMessages = validationMessages;
    }

    public static CategoryValidator create(Context context) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_category_mandatory, context.getString(R.string.validation_category_mandatory));
        messages.put(R.string.validation_category_duplicate, context.getString(R.string.validation_category_duplicate));

        return new CategoryValidator(messages);
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

}

