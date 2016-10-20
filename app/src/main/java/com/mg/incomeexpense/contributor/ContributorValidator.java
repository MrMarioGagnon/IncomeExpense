package com.mg.incomeexpense.contributor;

import android.content.Context;

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
 * Created by mario on 2016-07-21.
 */
public class ContributorValidator implements ObjectValidator {
    private final List<String> mNames;
    private final Map<Integer, String> mValidationMessages;

    public ContributorValidator(List<String> names, Map<Integer, String> validationMessages) {

        mNames = names;
        mValidationMessages = validationMessages;

    }

    public static ContributorValidator create(Context context, List<String> names) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_name_mandatory, context.getString(R.string.validation_name_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));

        return new ContributorValidator(names, messages);
    }

    private boolean isNameExists(String name) {

        return mNames.contains(name.toUpperCase());

    }

    public ValidationStatus Validate(ObjectBase objectToValidate) {

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

    public Boolean canDelete(ObjectBase objectToDelete, List<Account> accounts)
    {
        for(Account account : accounts){

            if(account.getContributors().contains(objectToDelete)){
                return false;
            }
        }

        return true;
    }
}
