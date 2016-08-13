package com.mg.incomeexpense.category;

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
public class CategoryValidator implements ObjectValidator {

    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public CategoryValidator(List<String> names, Map<Integer, String> validationMessages) {
        mNames = names;
        mValidationMessages = validationMessages;
    }

    public static CategoryValidator create(Context context, List<String> names) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_sub_categories_mandatory, context.getString(R.string.validation_sub_categories_mandatory));


        return new CategoryValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(ObjectBase objectToValidate){

        List<String> messages = new ArrayList<>();

        if (!(objectToValidate instanceof Category)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Category category = (Category) objectToValidate;
        String name = category.getName().trim();

        if (name.length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_name_mandatory));
        } else if (isNameExists(name)) {
            messages.add(mValidationMessages.get(R.string.validation_name_already_exists));
        }

        if (category.getSubCategoryCount() == 0) {
            messages.add((mValidationMessages.get(R.string.validation_sub_categories_mandatory)));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

}

